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
package org.shredzone.cilla.service.search;

import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Service for searching the index.
 *
 * @author Richard "Shred" Körber
 */
public interface SearchService {

    /**
     * Searches the page index using the given filter.
     *
     * @param filter
     *            {@link FilterModel} containing all filter parameters, or {@code null} to
     *            find all pages
     * @return {@link SearchResult} containing the search result
     */
    SearchResult search(FilterModel filter) throws CillaServiceException;

}
