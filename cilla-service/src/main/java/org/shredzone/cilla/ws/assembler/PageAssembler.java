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

import java.util.Date;

import javax.annotation.Resource;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.repository.LanguageDao;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.PageDto;
import org.shredzone.cilla.ws.page.PageInfoDto;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link PageDto} and {@link Page}.
 * <p>
 * Projections are supported, but provide {@link PageInfoDto} objects.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class PageAssembler extends AbstractAssembler<Page, PageDto> {

    private @Resource UserDao userDao;
    private @Resource LanguageDao languageDao;

    @Override
    public PageDto assemble(Page entity) throws CillaServiceException {
        PageDto dto = new PageDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());
        dto.setSubject(entity.getSubject());
        dto.setLanguageId(entity.getLanguage().getId());
        dto.setCreatorId(entity.getCreator().getId());
        dto.setCreatorLogin(entity.getCreator().getLogin());
        dto.setCreatorName(entity.getCreator().getName());
        dto.setCreation(entity.getCreation());
        dto.setModification(entity.getModification());
        dto.setPublication(entity.getPublication());
        dto.setExpiration(entity.getExpiration());
        dto.setSticky(entity.isSticky());
        dto.setHidden(entity.isHidden());
        dto.setCommentable(entity.getThread().isCommentable());
        dto.setDonatable(entity.isDonatable());
        dto.setPromoted(entity.isPromoted());
        dto.setChallenge(entity.getChallenge());
        dto.setResponsePattern(entity.getResponsePattern());

        if (entity.getTeaser() != null) {
            dto.setTeaser(entity.getTeaser().getText());
            dto.setTeaserFormat(entity.getTeaser().getFormat());
        }

        return dto;
    }

    @Override
    public void merge(PageDto dto, Page entity) throws CillaServiceException {
        super.merge(dto, entity);

        entity.setName(dto.getName());
        entity.setTitle(dto.getTitle());
        entity.setSubject(dto.getSubject());
        entity.setLanguage(languageDao.fetch(dto.getLanguageId()));
        entity.setCreator(userDao.fetch(dto.getCreatorId()));
        entity.setCreation(dto.getCreation());
        entity.setModification(new Date());
        entity.setPublication(dto.getPublication());
        entity.setExpiration(dto.getExpiration());
        entity.setSticky(dto.isSticky());
        entity.setHidden(dto.isHidden());
        entity.getThread().setCommentable(dto.isCommentable());
        entity.setDonatable(dto.isDonatable());
        entity.setPromoted(dto.isPromoted());

        if (dto.getTeaser() != null && dto.getTeaserFormat() != null) {
            entity.setTeaser(new FormattedText(dto.getTeaser(), dto.getTeaserFormat()));
        } else {
            entity.setTeaser(null);
        }

        if (dto.getChallenge() != null && !dto.getChallenge().isEmpty()
            && dto.getResponsePattern() != null && !dto.getResponsePattern().isEmpty()) {
            entity.setChallenge(dto.getChallenge());
            entity.setResponsePattern(dto.getResponsePattern());
        } else {
            entity.setChallenge(null);
            entity.setResponsePattern(null);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Only creates a projection for {@link PageInfoDto}.
     */
    @Override
    public ProjectionList projection() {
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.id(), "id");
        projection.add(Property.forName("name")        .as("name"));
        projection.add(Property.forName("title")       .as("title"));
        projection.add(Property.forName("subject")     .as("subject"));
        projection.add(Property.forName("creation")    .as("creation"));
        projection.add(Property.forName("modification").as("modification"));
        projection.add(Property.forName("publication") .as("publication"));
        projection.add(Property.forName("expiration")  .as("expiration"));
        projection.add(Property.forName("sticky")      .as("sticky"));
        projection.add(Property.forName("hidden")      .as("hidden"));
        projection.add(Property.forName("donatable")   .as("donatable"));
        projection.add(Property.forName("promoted")    .as("promoted"));
        projection.add(Property.forName("c.id")        .as("creatorId"));
        projection.add(Property.forName("c.login")     .as("creatorLogin"));
        projection.add(Property.forName("c.name")      .as("creatorName"));
        projection.add(Property.forName("t.commentable").as("commentable"));
        projection.add(Property.forName("language.id") .as("languageId"));
        return projection;
    }

}
