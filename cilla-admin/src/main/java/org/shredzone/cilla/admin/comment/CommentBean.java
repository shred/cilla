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
package org.shredzone.cilla.admin.comment;

import javax.annotation.Resource;

import org.shredzone.cilla.ws.comment.CommentDto;
import org.shredzone.cilla.ws.comment.CommentWs;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A bean for handling comments.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class CommentBean {

    private @Resource CommentWs commentWs;

    private CommentDto comment;

    /**
     * The currently selected {@link CommentDto}.
     */
    public CommentDto getComment()              { return comment; }
    public void setComment(CommentDto entry)    { comment = entry; }

    /**
     * Edits the selected {@link CommentDto}.
     */
    public String edit() throws CillaServiceException {
        return "/admin/comment/edit.xhtml";
    }

    /**
     * Deletes the selected {@link CommentDto}.
     */
    public void delete() throws CillaServiceException {
        commentWs.delete(comment.getId());
        comment = null;
    }

    /**
     * Commits changes to the selected {@link CommentDto}.
     */
    public String commit() throws CillaServiceException {
        commentWs.commit(comment);
        return "/admin/comment/list.xhtml";
    }

}
