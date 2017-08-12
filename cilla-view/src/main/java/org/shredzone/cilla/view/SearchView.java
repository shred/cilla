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
package org.shredzone.cilla.view;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.service.search.SearchService;
import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.annotation.Optional;
import org.shredzone.commons.view.annotation.Parameter;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ViewException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Views for searching content.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class SearchView extends AbstractView {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Value("${search.maxEntries}") int maxEntries;

    private @Resource SearchService searchService;

    /**
     * Shows the view result page.
     */
    @Framed
    @View(pattern = "/search", name="search")
    @View(pattern = "/search/${date}", name="search")
    public String searchView(HttpServletRequest req,
            @Optional @PathPart("date") DateRange date,
            @Optional @Parameter("p") PaginatorModel paginator,
            @Optional @Parameter("q") String query)
    throws ViewException {
        if (query == null) {
            return "view/search.jsp";
        }

        if (query.trim().isEmpty()) {
            req.setAttribute("message", "search.msg.noterm");
            return "view/search.jsp";
        }

        Locale locale = req.getLocale();
        if (locale == null) {
            locale = Locale.ENGLISH;
        }

        PaginatorModel usePaginator = paginator;
        if (usePaginator == null) {
            usePaginator = new PaginatorModel();
        }
        usePaginator.setPerPage(maxEntries);

        FilterModel filter = new FilterModel();
        filter.setLocale(locale);
        filter.setQuery(query);

        try {
            SearchResult result = searchService.search(filter);
            usePaginator.setCount(result.getCount());
            result.setPaginator(usePaginator);
            req.setAttribute("result", result);
        } catch (CillaServiceException ex) {
            log.debug("search for '{}' failed", query, ex);
            req.setAttribute("message", "search.msg.failed");
            req.setAttribute("details", ex.getCause().getLocalizedMessage());
        }

        req.setAttribute("paginator", usePaginator);
        req.setAttribute("query", query);

        return "view/search.jsp";
    }

}
