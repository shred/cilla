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
package org.shredzone.cilla.web.page;

import javax.servlet.http.HttpServletRequest;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Section;

/**
 * A service for handling comments related to a {@link Page} or {@link Section}.
 *
 * @author Richard "Shred" Körber
 */
public interface CommentFormHandler {

    /**
     * Handles a comment form for a {@link Page}.
     * <p>
     * Checks if the user is allowed to post a comment and if the comment is valid and the
     * captcha test was passed. Stores the comment in the database, then sends a
     * notification to the moderators.
     *
     * @param page
     *            {@link Page} to add the comment to
     * @param req
     *            {@link HttpServletRequest} with the comment form data
     */
    void handleComment(Page page, HttpServletRequest req);

}
