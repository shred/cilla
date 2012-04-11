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
package org.shredzone.cilla.ws.assembler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.annotation.Resource;

import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.repository.PictureDao;
import org.shredzone.cilla.core.repository.SectionDao;
import org.shredzone.cilla.service.PictureService;
import org.shredzone.cilla.service.SecurityService;
import org.shredzone.cilla.service.security.CillaUserDetails;
import org.shredzone.cilla.ws.TimeDefinition;
import org.shredzone.cilla.ws.assembler.annotation.SectionAssemblerType;
import org.shredzone.cilla.ws.exception.CillaParameterException;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.GallerySectionDto;
import org.shredzone.cilla.ws.page.PictureDto;
import org.springframework.stereotype.Component;

/**
 * {@link SectionAssembler} for {@link GallerySectionDto} and {@link GallerySection}.
 * <p>
 * Projections are <em>not</em> supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
@SectionAssemblerType(type = "gallery", entity = GallerySection.class, dto = GallerySectionDto.class)
public class GallerySectionAssembler extends AbstractSectionAssembler<GallerySection, GallerySectionDto> {

    private @Resource SectionDao sectionDao;
    private @Resource PictureDao pictureDao;
    private @Resource PictureAssembler pictureAssembler;
    private @Resource PictureService pictureService;
    private @Resource SecurityService securityService;

    @Override
    public GallerySectionDto assemble(GallerySection entity) throws CillaServiceException {
        GallerySectionDto dto = new GallerySectionDto();
        dto.setId(entity.getId());
        dto.setDefaultTimePrecision(entity.getDefaultTimePrecision());
        dto.setDefaultTimeZone(entity.getDefaultTimeZone());

        for (Picture picture : entity.getPictures()) {
            dto.getPictures().add(pictureAssembler.assemble(picture));
        }

        return dto;
    }

    @Override
    public void merge(GallerySectionDto dto, GallerySection entity) throws CillaServiceException {
        super.merge(dto, entity);
        mergeGallerySection(dto, entity);
        mergePictures(dto, entity);
    }

    private void mergeGallerySection(GallerySectionDto dto, GallerySection entity) {
        entity.setDefaultTimePrecision(dto.getDefaultTimePrecision());
        entity.setDefaultTimeZone(dto.getDefaultTimeZone());
    }

    private void mergePictures(GallerySectionDto dto, GallerySection entity) throws CillaServiceException {
        Set<Picture> removables = new HashSet<Picture>(entity.getPictures());
        entity.getPictures().clear();
        int cnt = 0;
        for (PictureDto picDto : dto.getPictures()) {
            Picture picture;

            if (picDto.isPersisted()) {
                picture = pictureDao.fetch(picDto.getId());
                pictureAssembler.merge(picDto, picture);
                removables.remove(picture);
                pictureService.updatePicture(
                    picture,
                    (picDto.getUploadFile() != null ? picDto.getUploadFile().getDataSource() : null)
                );

            } else {
                DataHandler dh = picDto.getUploadFile();
                if (dh == null) {
                    throw new CillaParameterException("new picture requires a file");
                }

                picture = new Picture();
                pictureAssembler.merge(picDto, picture);
                pictureService.addPicture(
                    entity,
                    picture,
                    dh.getDataSource()
                );
                picDto.setId(picture.getId());
            }

            picture.setSequence(cnt++);
            entity.getPictures().add(picture);
        }

        for (Picture picture : removables) {
            pictureService.removePicture(picture);
        }
    }

    @Override
    public GallerySection persistSection(GallerySectionDto dto, Page page) throws CillaServiceException {
        GallerySection sec;

        if (dto.isPersisted()) {
            sec = (GallerySection) sectionDao.fetch(dto.getId());
            merge(dto, sec);
            updateSection(sec);

        } else {
            sec = new GallerySection();
            mergeGallerySection(dto, sec);      // 1. set up the GallerySection
            addSection(page, sec);              // 2. persist GallerySection, create ID
            mergePictures(dto, sec);            // 3. add pictures to this GallerySection
            dto.setId(sec.getId());
        }

        return sec;
    }

    @Override
    public GallerySectionDto createSection() throws CillaServiceException {
        GallerySection sec = new GallerySection();
        sec.setDefaultTimePrecision(TimeDefinition.NONE);

        CillaUserDetails cud = securityService.getAuthenticatedUser();
        sec.setDefaultTimeZone(cud != null ? cud.getTimeZone() : TimeZone.getDefault());

        return assemble(sec);
    }

    @Override
    public void delete(GallerySection entity) throws CillaServiceException {
        List<Picture> copied = new ArrayList<Picture>(entity.getPictures());
        for (Picture picture : copied) {
            pictureService.removePicture(picture);
        }
        removeSection(entity);
    }

}
