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
package org.shredzone.cilla.web.info;

import org.shredzone.cilla.core.model.Page;

/**
 * Service for fetching {@link PageInfo}. Long-term caching is used.
 *
 * @author Richard "Shred" Körber
 */
public interface PageInfoService {

    /**
     * Creates a {@link PageInfo} object for a {@link Page}.
     *
     * @param page
     *            {@link Page} to get the {@link PageInfo} for
     * @return {@link PageInfo} of that {@link Page}
     */
    PageInfo getPageInfo(Page page);

}
