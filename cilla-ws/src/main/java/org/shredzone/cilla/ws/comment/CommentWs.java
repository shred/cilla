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
package org.shredzone.cilla.ws.comment;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Service for handling Comments.
 *
 * @author Richard "Shred" Körber
 */
@WebService
public interface CommentWs {

    /**
     * Fetches a {@link CommentDto} by its ID.
     *
     * @param commentId
     *            Comment ID
     * @return {@link CommentDto}, or {@code null} if it does not exist
     */
    CommentDto fetch(@WebParam(name = "commentId") long commentId) throws CillaServiceException;

    /**
     * Counts the number of all comments (also unpublished ones).
     */
    long count();

    /**
     * Lists all {@link CommentDto} matching the criteria.
     *
     * @param criteria
     *            {@link ListRange}, or {@code null} for all
     * @return List of matching {@link CommentDto}
     */
    List<CommentDto> list(@WebParam(name = "criteria") ListRange criteria);

    /**
     * Creates a new {@link CommentDto} for the given page.
     *
     * @param pageId
     *            ID of the Page the comment will be added to
     * @return {@link CommentDto} that was created
     */
    CommentDto createNew(@WebParam(name = "pageId") long pageId) throws CillaServiceException;

    /**
     * Commits a {@link CommentDto}.
     *
     * @param comment
     *            {@link CommentDto} to be committed
     * @return Committed {@link CommentDto}, must be used from now on
     */
    CommentDto commit(@WebParam(name = "comment") CommentDto comment) throws CillaServiceException;

    /**
     * Deletes a Comment by its ID. It will delete the entire thread (this is, also all
     * replies made to this comment).
     *
     * @param commentId
     *            Comment ID to be deleted
     */
    void delete(@WebParam(name = "commentId") long commentId) throws CillaServiceException;

}
