/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2012 Richard "Shred" Körber
 *   http://cilla.shredzone.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.shredzone.cilla.ws.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.jws.WebService;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.repository.CategoryDao;
import org.shredzone.cilla.core.repository.MediumDao;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.repository.PictureDao;
import org.shredzone.cilla.core.repository.TagDao;
import org.shredzone.cilla.service.PageService;
import org.shredzone.cilla.service.PictureService;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.ws.AbstractWs;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.SectionFacade;
import org.shredzone.cilla.ws.assembler.CategoryAssembler;
import org.shredzone.cilla.ws.assembler.MediumAssembler;
import org.shredzone.cilla.ws.assembler.PageAssembler;
import org.shredzone.cilla.ws.assembler.PictureAssembler;
import org.shredzone.cilla.ws.category.CategoryDto;
import org.shredzone.cilla.ws.exception.CillaNotFoundException;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.MediumDto;
import org.shredzone.cilla.ws.page.PageDto;
import org.shredzone.cilla.ws.page.PageInfoDto;
import org.shredzone.cilla.ws.page.PageWs;
import org.shredzone.cilla.ws.page.PictureDto;
import org.shredzone.cilla.ws.page.SectionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link PageWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "PageWs",
    endpointInterface = "org.shredzone.cilla.ws.page.PageWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class PageWsImpl extends AbstractWs implements PageWs {

    private @Value("${site.previewUrl}") String previewUrl;

    private @Resource PageAssembler pageAssembler;
    private @Resource MediumAssembler mediumAssembler;
    private @Resource PictureAssembler pictureAssembler;
    private @Resource CategoryAssembler categoryAssembler;

    private @Resource SectionFacade sectionFacade;

    private @Resource PageService pageService;
    private @Resource PictureService pictureService;
    private @Resource LinkService linkService;

    private @Resource PageDao pageDao;
    private @Resource TagDao tagDao;
    private @Resource CategoryDao categoryDao;
    private @Resource MediumDao mediumDao;
    private @Resource PictureDao pictureDao;

    @Override
    public PageDto fetch(long id) throws CillaServiceException {
        Page entity = pageDao.fetch(id);
        if (entity == null) {
            throw new CillaNotFoundException("page", id);
        }

        PageDto dto = pageAssembler.assemble(entity);
        dto.setSections(sectionFacade.assembleSections(entity.getSections()));
        dto.setMedia(mediumAssembler.bulkAssemble(mediumDao.fetchAll(entity)));
        dto.setCategories(categoryAssembler.bulkAssemble(entity.getCategories()));

        for (Tag tag : entity.getTags()) {
            dto.getTags().add(tag.getName());
        }

        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        return pageDao.countAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PageInfoDto> list(ListRange lr) {
        Criteria crit = pageDao.criteria()
            .createAlias("creator", "c")
            .addOrder(Order.desc("creation"))
            .setProjection(pageAssembler.projection())
            .setResultTransformer(new AliasToBeanResultTransformer(PageInfoDto.class));

        applyListRange(lr, "creation", true, crit);

        return crit.list();
    }

    @Override
    public PageDto createNew() throws CillaServiceException {
        return pageAssembler.assemble(pageService.createNew());
    }

    @Override
    public PageDto commit(PageDto dto) throws CillaServiceException {
        if (dto.isPersisted()) {
            Page page = pageDao.fetch(dto.getId());
            pageAssembler.merge(dto, page);
            commitSections(dto, page);
            commitMedium(dto, page);
            commitCategories(dto, page);
            commitTags(dto, page);
            pageService.update(page);

        } else {
            Page page = new Page();
            pageAssembler.merge(dto, page);
            pageService.create(page);
            dto.setId(page.getId());
            commitSections(dto, page);
            commitMedium(dto, page);
            commitCategories(dto, page);
            commitTags(dto, page);
        }

        return dto;
    }

    @Override
    public void delete(long pageId) throws CillaServiceException {
        pageService.remove(pageDao.fetch(pageId));
    }

    @Override
    public String previewUrl(long pageId) throws CillaServiceException {
        Page pageEntity = pageDao.fetch(pageId);
        if (pageEntity == null) {
            throw new CillaNotFoundException("page", pageId);
        }

        return linkService.linkTo().page(pageEntity).anchor("content").base(previewUrl).toString();
    }

    @Override
    public Collection<String> getSectionTypes() {
        return sectionFacade.getSectionTypes();
    }

    @Override
    public SectionDto createNewSection(String type) throws CillaServiceException {
        return sectionFacade.createSection(type);
    }

    @Override
    public MediumDto createNewMedium() throws CillaServiceException {
        return mediumAssembler.assemble(pageService.createNewMedium());
    }

    @Override
    public PictureDto createNewPicture() throws CillaServiceException {
        return pictureAssembler.assemble(pictureService.createNew());
    }

    @Override
    public DataHandler getMediumImage(long mediumId, ImageProcessing process) throws CillaServiceException {
        Medium medium = mediumDao.fetch(mediumId);
        if (medium == null) {
            throw new CillaNotFoundException("medium", mediumId);
        }

        return new DataHandler(pageService.getMediumImage(medium, process));
    }

    @Override
    public DataHandler getGalleryImage(long pictureId, ImageProcessing process) throws CillaServiceException {
        Picture pic = pictureDao.fetch(pictureId);
        if (pic == null) {
            throw new CillaNotFoundException("picture", pictureId);
        }

        return new DataHandler(pictureService.getImage(pic, process));
    }

    @Override
    public List<String> proposeSubjects(String query, int limit) {
        return pageDao.proposeSubjects(query, limit);
    }

    /**
     * Commits all sections of a page.
     *
     * @param dto
     *            {@link PageDto} containing the sections
     * @param entity
     *            {@link Page} to commit the sections to
     */
    private void commitSections(PageDto dto, Page entity) throws CillaServiceException {
        Set<Section> deletableSec = new HashSet<Section>(entity.getSections());
        for (SectionDto sec : dto.getSections()) {
            Section persistedSec = sectionFacade.persistSection(sec, entity);
            deletableSec.remove(persistedSec);
        }
        for (Section deleteSec : deletableSec) {
            sectionFacade.deleteSection(deleteSec);
        }
    }

    /**
     * Commits all media of a page.
     *
     * @param dto
     *            {@link PageDto} containing the media
     * @param entity
     *            {@link Page} to commit the media to
     */
    private void commitMedium(PageDto dto, Page entity) throws CillaServiceException {
        Set<Medium> deletableMedia = new HashSet<Medium>(mediumDao.fetchAll(entity));
        for (MediumDto mediumDto : dto.getMedia()) {
            if (mediumDto.isPersisted()) {
                Medium medium = mediumDao.fetch(mediumDto.getId());
                deletableMedia.remove(medium);
                mediumAssembler.merge(mediumDto, medium);
                if (mediumDto.getUploadMediumFile() != null) {
                    pageService.updateMedium(entity, medium, mediumDto.getUploadMediumFile().getDataSource());
                }

            } else {
                Medium medium = new Medium();
                mediumAssembler.merge(mediumDto, medium);
                pageService.addMedium(entity, medium, mediumDto.getUploadMediumFile().getDataSource());
                mediumDto.setId(medium.getId());
            }
        }
        for (Medium deletable : deletableMedia) {
            pageService.removeMedium(entity, deletable);
        }
    }

    /**
     * Commits all categories of a page.
     *
     * @param dto
     *            {@link PageDto} containing the categories
     * @param entity
     *            {@link Page} to commit the categories to
     */
    private void commitCategories(PageDto dto, Page entity) throws CillaServiceException {
        Set<Category> catSet = new HashSet<Category>();
        for (CategoryDto catDto : dto.getCategories()) {
            Category cat = categoryDao.fetch(catDto.getId());
            if (cat != null) {
                catSet.add(cat);
            }
        }
        entity.setCategories(catSet);
    }

    /**
     * Commits all tags of a page.
     *
     * @param dto
     *            {@link PageDto} containing the tags
     * @param entity
     *            {@link Page} to commit the tags to
     */
    private void commitTags(PageDto dto, Page entity) throws CillaServiceException {
        Set<Tag> tagSet = new HashSet<Tag>();
        for (String tag : dto.getTags()) {
            tagSet.add(tagDao.fetchOrCreate(tag));
        }
        entity.setTags(tagSet);
    }

}
