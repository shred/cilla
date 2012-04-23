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

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.repository.CommentDao;
import org.shredzone.cilla.core.repository.HeaderDao;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.repository.PictureDao;
import org.shredzone.cilla.service.CommentService;
import org.shredzone.cilla.ws.AbstractWs;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.assembler.CommentAssembler;
import org.shredzone.cilla.ws.comment.CommentDto;
import org.shredzone.cilla.ws.comment.CommentWs;
import org.shredzone.cilla.ws.exception.CillaParameterException;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link CommentWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "CommentWs",
    endpointInterface = "org.shredzone.cilla.ws.comment.CommentWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class CommentWsImpl extends AbstractWs implements CommentWs {

    private @Resource CommentAssembler commentAssembler;
    private @Resource CommentService commentService;
    private @Resource CommentDao commentDao;
    private @Resource PageDao pageDao;
    private @Resource PictureDao pictureDao;
    private @Resource HeaderDao headerDao;

    @Override
    public CommentDto fetch(long id) throws CillaServiceException {
        Comment comment = commentDao.fetch(id);
        return (comment != null ? commentAssembler.assemble(comment) : null);
    }

    @Override
    public long count() {
        return commentDao.countAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CommentDto> list(ListRange lr) {
        Criteria crit = commentDao.criteria()
            .createAlias("thread", "t")
            .createAlias("creator", "c", CriteriaSpecification.LEFT_JOIN)
            .setProjection(commentAssembler.projection())
            .setResultTransformer(new AliasToBeanResultTransformer(CommentDto.class));

        applyListRange(lr, "creation", true, crit);

        return crit.list();
    }

    @Override
    public CommentDto createForPage(long pageId) throws CillaServiceException {
        Page page = pageDao.fetch(pageId);
        if (page == null) {
            throw new CillaParameterException("Page not found: " + pageId);
        }
        return commentAssembler.assemble(commentService.createNew(page));
    }

    @Override
    public CommentDto createForPicture(long pictureId) throws CillaServiceException {
        Picture picture = pictureDao.fetch(pictureId);
        if (picture == null) {
            throw new CillaParameterException("Picture not found: " + pictureId);
        }
        return commentAssembler.assemble(commentService.createNew(picture));
    }

    @Override
    public CommentDto createForHeader(long headerId) throws CillaServiceException {
        Header header = headerDao.fetch(headerId);
        if (header == null) {
            throw new CillaParameterException("Header not found: " + headerId);
        }
        return commentAssembler.assemble(commentService.createNew(header));
    }

    @Override
    public CommentDto commit(CommentDto dto) throws CillaServiceException {
        if (dto.isPersisted()) {
            Comment comment = commentDao.fetch(dto.getId());
            commentAssembler.merge(dto, comment);

        } else {
            Comment comment = new Comment();
            commentAssembler.merge(dto, comment);
            commentService.postSilently(comment);
            dto.setId(comment.getId());
        }

        return dto;
    }

    @Override
    public void delete(long commentId) throws CillaServiceException {
        Comment comment = commentDao.fetch(commentId);
        if (comment == null) {
            throw new CillaParameterException("Comment not found: " + commentId);
        }
        commentService.remove(comment);
    }

}
