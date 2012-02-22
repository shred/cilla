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

import javax.annotation.Resource;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.repository.CommentDao;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.ws.comment.CommentDto;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link CommentDto} and {@link Comment}.
 * <p>
 * Projections are supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class CommentAssembler extends AbstractAssembler<Comment, CommentDto> {

    private @Resource CommentDao commentDao;
    private @Resource PageDao pageDao;
    private @Resource UserDao userDao;

    @Override
    public CommentDto assemble(Comment entity) throws CillaServiceException {
        CommentDto dto = new CommentDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setMail(entity.getMail());
        dto.setUrl(entity.getUrl());
        dto.setText(entity.getText().getText());
        dto.setTextFormat(entity.getText().getFormat());
        dto.setCreation(entity.getCreation());
        dto.setPublished(entity.isPublished());
        dto.setPageId(entity.getPage().getId());
        dto.setPageTitle(entity.getPage().getTitle());

        if (entity.getReplyTo() != null) {
            dto.setReplyToId(entity.getReplyTo().getId());
        }

        if (entity.getCreator() != null) {
            dto.setCreatorId(entity.getCreator().getId());
            dto.setCreatorLogin(entity.getCreator().getLogin());
            dto.setCreatorName(entity.getCreator().getName());
        }

        return dto;
    }

    @Override
    public void merge(CommentDto dto, Comment entity) throws CillaServiceException {
        super.merge(dto, entity);

        if (dto.getCreatorId() != null) {
            entity.setCreator(userDao.fetch(dto.getCreatorId()));
        } else {
            entity.setCreator(null);
        }

        if (dto.getReplyToId() != null) {
            entity.setReplyTo(commentDao.fetch(dto.getReplyToId()));
        } else {
            entity.setReplyTo(null);
        }

        entity.setCreation(dto.getCreation());
        entity.setMail(dto.getMail());
        entity.setName(dto.getName());
        entity.setPage(pageDao.fetch(dto.getPageId()));
        entity.setPublished(dto.isPublished());
        entity.setText(new FormattedText(dto.getText(), dto.getTextFormat()));
        entity.setUrl(dto.getUrl());
    }

    @Override
    public ProjectionList projection() {
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.id(), "id");
        projection.add(Property.forName("name")        .as("name"));
        projection.add(Property.forName("mail")        .as("mail"));
        projection.add(Property.forName("url")         .as("url"));
        projection.add(Property.forName("text.text")   .as("text"));
        projection.add(Property.forName("text.format") .as("textFormat"));
        projection.add(Property.forName("creation")    .as("creation"));
        projection.add(Property.forName("published")   .as("published"));
        projection.add(Property.forName("replyTo.id")  .as("replyToId"));
        projection.add(Property.forName("p.id")        .as("pageId"));
        projection.add(Property.forName("p.title")     .as("pageTitle"));
        projection.add(Property.forName("c.id")        .as("creatorId"));
        projection.add(Property.forName("c.login")     .as("creatorLogin"));
        projection.add(Property.forName("c.name")      .as("creatorName"));
        return projection;
    }

}
