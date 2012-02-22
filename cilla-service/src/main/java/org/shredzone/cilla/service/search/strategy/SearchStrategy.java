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
package org.shredzone.cilla.service.search.strategy;

import java.util.Calendar;
import java.util.Set;

import org.shredzone.cilla.service.search.impl.SearchResultImpl;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * A strategy for filling {@link SearchResultImpl}.
 *
 * @author Richard "Shred" Körber
 */
public interface SearchStrategy {

    /**
     * Counts the number of results and sets the {@link SearchResultImpl} accordingly.
     */
    void count(SearchResultImpl result) throws CillaServiceException;

    /**
     * Searches for matching pages and sets the {@link SearchResultImpl} accordingly.
     */
    void search(SearchResultImpl result) throws CillaServiceException;

    /**
     * Fetches matching page days.
     *
     * @param result
     *            {@link SearchResultImpl} with the query parameters
     * @param calendar
     *            {@link Calendar} with the year and month
     * @return Set of days
     */
    Set<Integer> fetchPageDays(SearchResultImpl result, Calendar calendar) throws CillaServiceException;

}
