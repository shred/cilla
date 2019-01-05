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
package org.shredzone.cilla.core.repository.impl;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.repository.SearchDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Hibernate and Lucene implementation of {@link SearchDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("searchDao")
@Transactional
public class SearchDaoHibImpl implements SearchDao {

    private @Resource PageDao pageDao;
    private @Resource SessionFactory sessionFactory;

    @Override
    public Query parseQuery(String query, Locale locale) throws ParseException{
        FullTextSession fullTextSession = getFullTextSession();

        MultiFieldQueryParser parser = new MultiFieldQueryParser(
                        new String[] {"text", "title"},
                        fullTextSession.getSearchFactory().getAnalyzer(Page.ANALYZER));

        if (locale != null) {
            parser.setLocale(locale);
        }

        return parser.parse(query);
    }

    @Override
    public int count(Query query, Criteria crit) {
        // This is the only way to reliable get a result counter if restricting
        // criteria apply.
        return (int) search(query, crit).stream()
                .filter(SearchDaoHibImpl::isValidResult)
                .count();
    }

    @Override
    public List<Page> fetch(Query query, Criteria crit) {
        return Collections.unmodifiableList(search(query, crit).stream()
                .filter(SearchDaoHibImpl::isValidResult)
                .collect(toList()));
    }

    /**
     * Performs the search.
     * <p>
     * Due to a bug in Hibernate Search, the resulting list may contain unpublished pages
     * if only one hit was found. It is recommended to check the resulting list and filter
     * all unpublished pages, and other pages that are not supposed to be found.
     *
     * @param query
     *            {@link Query} to perform
     * @param crit
     *            {@link Criteria} containing search parameters
     * @return {@link List} of {@link Page} found
     */
    @SuppressWarnings("unchecked")
    private List<Page> search(Query query, Criteria crit) {
        assertCriteriaEntity(crit);

        FullTextSession fullTextSession = getFullTextSession();
        FullTextQuery fq = fullTextSession.createFullTextQuery(query, Page.class);
        fq.setCriteriaQuery(crit != null ? crit : pageDao.criteria());

        return fq.list();
    }

    /**
     * Checks if the given {@link Page} is allowed to be seen in a search result.
     *
     * @param page
     *            {@link Page} to check
     * @return {@code true}: valid search result, {@code false}: do not show as result
     */
    private static boolean isValidResult(Page page) {
        return page.isPublished() && !page.isRestricted();
    }

    /**
     * Asserts that the given {@link Criteria} is bound to the correct entity.
     *
     * @param crit
     *            {@link Criteria} to test
     */
    private void assertCriteriaEntity(Criteria crit) {
        if (crit != null && crit instanceof CriteriaImpl) {
            CriteriaImpl impl = (CriteriaImpl) crit;
            String entity = impl.getEntityOrClassName();
            if (!(Page.class.getName().equals(entity))) { //NOSONAR: false positive
                throw new IllegalArgumentException("Criteria not bound to Page, but " + entity);
            }
        }
    }

    /**
     * Gets the current {@link Session}.
     *
     * @return Current {@link Session}
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Gets a {@link FullTextSession} of the current {@link Session}.
     *
     * @return Current {@link FullTextSession}
     */
    protected FullTextSession getFullTextSession() {
        return Search.getFullTextSession(getCurrentSession());
    }

}
