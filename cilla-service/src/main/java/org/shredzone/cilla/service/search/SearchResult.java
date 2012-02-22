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

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Contains the result of a search request.
 * <p>
 * Many methods are lazily initialized and cached, so this class can be used quite
 * efficiently.
 *
 * @author Richard "Shred" Körber
 */
public interface SearchResult {

    /**
     * {@link FilterModel} to be used for searching
     */
    void setFilter(FilterModel model);
    FilterModel getFilter();

    /**
     * {@link PaginatorModel} to be used for requesting pages.
     */
    void setPaginator(PaginatorModel paginator);
    PaginatorModel getPaginator();

    /**
     * Returns the number of pages that were matching the {@link FilterModel}.
     */
    int getCount() throws CillaServiceException;

    /**
     * Returns the {@link Page} that were found. The paginator settings are used.
     */
    List<Page> getPages() throws CillaServiceException;

    /**
     * For search requests with a query set, a HTML string is returned for each page
     * that was found. The HTML string contains extracts of the page, with the query
     * string highlighted.
     */
    List<String> getHighlighted() throws CillaServiceException;

    /**
     * Returns the effective set of {@link Category} affected by the {@link FilterModel}.
     */
    Collection<Category> getEffectiveCategories() throws CillaServiceException;

    /**
     * Fetches all days having at least one matching page in the result set. This method
     * is useful for rendering a calendar of a month of the search result.
     *
     * @param calendar
     *            {@link Calendar} with year and month to fetch the page days for
     * @return Set of {@link Integer} of days that match the search.
     */
    Set<Integer> fetchPageDays(Calendar calendar) throws CillaServiceException;

}
