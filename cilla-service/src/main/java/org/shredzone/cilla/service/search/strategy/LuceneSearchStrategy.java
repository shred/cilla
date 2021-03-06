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

import java.io.StringReader;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.search.Search;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.SearchDao;
import org.shredzone.cilla.core.search.PageBridge;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.impl.SearchResultImpl;
import org.shredzone.cilla.service.search.renderer.SearchResultRenderer;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A {@link SearchStrategy} that uses Lucene for finding. This strategy is used when a
 * query string is set in the {@link FilterModel}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class LuceneSearchStrategy extends AbstractSearchStrategy {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource SearchDao searchDao;
    private @Resource SearchResultRenderer searchResultRenderer;
    private @Resource SessionFactory sessionFactory;

    @Override
    public void count(SearchResultImpl result) throws CillaServiceException {
        Query query = createQuery(result);
        Criteria crit = createCriteria(result);

        result.setCount(searchDao.count(query, crit));
    }

    @Override
    public void search(SearchResultImpl result) throws CillaServiceException {
        Query query = createQuery(result);
        Criteria crit = createPaginatedCriteria(result);

        List<Page> resultset = searchDao.fetch(query, crit);
        result.setPages(resultset);
        result.setHighlighted(createHighlights(query, resultset));
    }

    /**
     * Creates a {@link Query} for the search result.
     *
     * @param result
     *            {@link SearchResultImpl} to create a {@link Query} for
     * @return {@link Query} that was created
     */
    private Query createQuery(SearchResultImpl result) throws CillaServiceException {
        FilterModel filter = result.getFilter();
        try {
            return searchDao.parseQuery(filter.getQuery(), filter.getLocale());
        } catch (ParseException ex) {
            throw new CillaServiceException("Could not parse query '" + filter.getQuery() + "'" , ex);
        }
    }

    /**
     * Creates a list of highlights for a search result.
     *
     * @param pq
     *            {@link Query} that was used
     * @param result
     *            List of {@link Page} results
     * @return matching list of text extracts with highlights
     */
    private List<String> createHighlights(Query pq, List<Page> result) {
        QueryScorer scorer = new QueryScorer(pq, "text");
        Fragmenter fragmenter = searchResultRenderer.createFragmenter(scorer);
        Formatter formatter = searchResultRenderer.createFormatter();

        Highlighter hilighter = new Highlighter(formatter, scorer);
        hilighter.setTextFragmenter(fragmenter);

        PageBridge bridge = new PageBridge();

        Analyzer pageAnalyzer = Search.getFullTextSession(sessionFactory.getCurrentSession())
                        .getSearchFactory()
                        .getAnalyzer(Page.ANALYZER);

        return result.stream()
                .map(bridge::objectToString)
                .map(plain -> highlight(plain, hilighter, pageAnalyzer))
                .collect(Collectors.toList());
    }

    /**
     * Highlight the contents by search result.
     *
     * @param content
     *            Plain text content to highlight
     * @param hilighter
     *            {@link Highlighter} to use
     * @param pageAnalyzer
     *            This session's {@link Page} {@link Analyzer}.
     * @return Highlighted content, or original content if there was nothing to highlight
     */
    private String highlight(String content, Highlighter hilighter, Analyzer pageAnalyzer) {
        try {
            TokenStream tokenStream = pageAnalyzer.tokenStream("text", new StringReader(content));

            StringBuilder sb = new StringBuilder();
            sb.append(searchResultRenderer.getHeader());
            sb.append(hilighter.getBestFragments(
                            tokenStream,
                            content,
                            searchResultRenderer.getMaxResults(),
                            searchResultRenderer.getSeparator()
            ));
            sb.append(searchResultRenderer.getFooter());

            if (sb.length() == 0) {
                // Nothing to highlight? Just return the content.
                return content;
            }

            return sb.toString();
        } catch (Exception ex) {
            // Just ignore the error and return the unhighlighted text
            log.warn("highlighting failed", ex);
            return content;
        }
    }

}
