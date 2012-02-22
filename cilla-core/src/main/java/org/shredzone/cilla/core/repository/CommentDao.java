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
import org.shredzone.cilla.core.model.Page;

/**
 * DAO for {@link Comment} entities.
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
     * Fetches all published {@link Comment} of a {@link Page}. Comments with a sectionRef
     * are excluded. The comments are sorted chronologically.
     *
     * @param page
     *            {@link Page} to fetch the {@link Comment} of
     * @return List of {@link Comment} for the {@link Page}. May be empty but never
     *         {@code null}.
     */
    List<Comment> fetchPublishedComments(Page page);

    /**
     * Fetches all {@link Comment} of a {@link Page}. Even unpublished comments and those
     * with a sectionRef are returned. This is, all {@link Comment} that refer to the
     * given {@link Page} are returned.
     *
     * @param page
     *            {@link Page} to fetch the {@link Comment} of
     * @return List of {@link Comment} for the {@link Page}. May be empty but never
     *         {@code null}.
     */
    List<Comment> fetchComments(Page page);

}
