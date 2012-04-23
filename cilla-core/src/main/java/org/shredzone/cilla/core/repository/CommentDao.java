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
package org.shredzone.cilla.core.repository;

import java.util.List;

import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.CommentThread;
import org.shredzone.cilla.core.model.is.Commentable;

/**
 * DAO for {@link Comment} entities and {@link CommentThread} entities. The latter ones
 * are managed by their reference and thus do not need to be created or deleted
 * invididually.
 *
 * @author Richard "Shred" Körber
 */
public interface CommentDao extends BaseDao<Comment> {

    /**
     * Fetches all direct replies on the given {@link Comment}, even unpublished ones.
     * Comments are sorted chronologically.
     *
     * @param comment
     *            {@link Comment} to find replies for
     * @return List of reply {@link Comment}.
     */
    List<Comment> fetchReplies(Comment comment);

    /**
     * Fetches the entry that uses the given {@link CommentThread}.
     *
     * @param thread
     *            {@link CommentThread} to fetch the reference entry for
     * @return Reference to the {@link Commentable} entry
     */
    Commentable fetchCommentable(CommentThread thread);

}
