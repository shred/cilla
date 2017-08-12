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
import java.util.Locale;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.shredzone.cilla.core.model.Page;

/**
 * A special DAO that gives access to the search index. For searching, Lucene is used.
 *
 * @author Richard "Shred" Körber
 */
public interface SearchDao {

    /**
     * Parses a query string and returns a {@link Query} object.
     *
     * @param query
     *            Query string to be parsed
     * @param locale
     *            {@link Locale} of the requesting user, used for optimizing the search
     *            query. May be {@code null}.
     * @return {@link Query} that was created
     * @throws ParseException
     *             if the query string could not be parsed
     */
    Query parseQuery(String query, Locale locale) throws ParseException;

    /**
     * Counts the number of pages matching the query. Note that the distinct number of
     * pages is returned, not the number of individual hits.
     *
     * @param query
     *            {@link Query} to be used for counting
     * @param crit
     *            additional {@link Criteria} to further limit the search. May be
     *            {@code null}. Must be a {@link Criteria} for {@link Page} entities.
     * @return distinct number of matching pages
     */
    int count(Query query, Criteria crit);

    /**
     * Fetches all pages matching the query. Depending on the search term, the result may
     * grow very large. Using {@link Criteria#setMaxResults(int)} is strongly recommended!
     *
     * @param query
     *            {@link Query} to be used for searching
     * @param crit
     *            additional {@link Criteria} to further limit the search. May be
     *            {@code null}. Must be a {@link Criteria} for {@link Page} entities.
     * @return Pages that matched the query, sorted by a random sort order unless a sort
     *         order was given in the {@link Criteria}
     */
    List<Page> fetch(Query query, Criteria crit);

}
