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
package org.shredzone.cilla.service;

import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.is.Commentable;

/**
 * A service for all kind of {@link Comment} operations.
 *
 * @author Richard "Shred" Körber
 */
public interface CommentService {

    /**
     * Creates a new, empty {@link Comment}. If a user is authenticated, the
     * {@link Comment} is created on behalf of this user.
     *
     * @param commentable
     *            {@link Commentable} the comment will belong to
     * @return Created {@link Comment}
     */
    Comment createNew(Commentable commentable);

    /**
     * Posts a comment. The comment is changed according to user rights and limitations.
     * After posting, no notification is sent. This method is mainly designed for
     * administrative purposes.
     *
     * @param comment
     *            {@link Comment} to be posted
     */
    void postSilently(Comment comment);

    /**
     * Posts a comment. The comment is changed according to user rights and limitations.
     * After posting the comment, a notification is sent to all moderators.
     *
     * @param comment
     *            {@link Comment} to be posted
     * @param request
     *            request data to be forwarded to the mail template
     */
    void postComment(Comment comment, Object request);

    /**
     * Removes a comment. It also removes all comments that were posted as a reply to this
     * comment.
     *
     * @param comment
     *            {@link Comment} to be deleted
     */
    void remove(Comment comment);

    /**
     * Removes all comments posted to the given thread.
     *
     * @param commentable
     *            {@link Commentable} to delete all comments of
     */
    void removeAll(Commentable commentable);

}
