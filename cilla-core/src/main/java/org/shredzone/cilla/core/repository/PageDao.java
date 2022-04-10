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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.shredzone.cilla.core.model.Page;

/**
 * DAO for {@link Page} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface PageDao extends BaseDao<Page> {

    /**
     * Fetches a {@link Page} by its name. If the page was not found, {@code null} is
     * returned.
     *
     * @param name
     *            Page name
     * @return Page or {@code null}
     */
    Page fetchByName(String name);

    /**
     * Fetches all public {@link Page}. Public pages are published and not expired. Hidden
     * pages are returned as well. Restricted pages are not returned. Ordered by date,
     * descendingly.
     *
     * @return List of all public {@link Page}.
     */
    List<Page> fetchAllPublic();

    /**
     * Fetches all public {@link Page} that were published since the given date. Public
     * pages are published and not expired. Hidden pages are NOT returned. Restricted
     * pages are not returned. Ordered by date, descendingly.
     *
     * @param since
     *         Earliest publication date
     * @return List of all public {@link Page} published since that day
     */
    List<Page> fetchAllPublicSince(Date since);

    /**
     * Fetches the minimum and maximum modification date of all published pages.
     *
     * @return Array having two entries. Index 0 contains the minimum, and index 1
     *         contains the maximum modification date. Note that both can be {@code null}
     *         if there are no published articles yet.
     */
    Date[] fetchMinMaxModification();

    /**
     * Fetches the {@link Date} of the last {@link Page} in the previous month and the
     * first {@link Page} in the next month of the given {@link Date}. This method is
     * useful for calendars to put a link to that previous or next month that actually
     * contains a {@link Page}.
     *
     * @param calendar
     *            {@link Calendar} of the current month
     * @return Array having two entries. Index 0 contains the last {@link Date} of the
     *         previous month, or {@code null} if there are no pages before the current
     *         month. Index 1 contains the first {@link Date} of the next month, or
     *         {@code null} if there are no pages after the current month.
     */
    Date[] fetchPrevNextMonth(Calendar calendar);

    /**
     * Fetches the previous published page to the given page.
     *
     * @param page
     *            Current {@link Page}
     * @return Previous {@link Page}, or {@code null} if there is none.
     */
    Page fetchPreviousPage(Page page);

    /**
     * Fetches the next published page to the given page.
     *
     * @param page
     *            Current {@link Page}
     * @return Next {@link Page}, or {@code null} if there is none.
     */
    Page fetchNextPage(Page page);

    /**
     * Fetches all pages that cover the same subject as the given page. The pages are
     * returned in chronological order. Only published pages are returned.
     *
     * @param page
     *            {@link Page} to use
     * @return List of {@link Page} covering the same subject. May be empty, but is never
     *         {@code null}.
     */
    List<Page> fetchSameSubject(Page page);

    /**
     * Fetches a list of all subject strings that are currently used. The list is sorted
     * alphabetically.
     *
     * @return List of all used subjects. May be empty, but is never {@code null}.
     */
    List<String> fetchAllSubjects();

    /**
     * Returns a list of proposed subjects matching the given query.
     *
     * @param query
     *            Query string
     * @param limit
     *            Maximum number of proposals
     * @return List of proposed subjects
     */
    List<String> proposeSubjects(String query, int limit);

    /**
     * Fetches all pages where the publishstate flag does not correspond with the actual
     * publish state.
     *
     * @return List of {@link Page} with a wrong publish state flag
     */
    List<Page> fetchBadPublishState();

    /**
     * Fetches all pages having the property with the key set to the given value. If the
     * value is {@code null}, all pages having the property set at all will be returned.
     *
     * @param key
     *            Property key to find, must not be {@code null}
     * @param value
     *            Property value to find, or {@code null} if the value does not matter
     * @return List of {@link Page} matching the query
     */
    List<Page> fetchHavingProperty(String key, String value);

}
