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

import javax.annotation.Resource;
import javax.servlet.ServletRequest;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Views for listing pages by their categories, tags or users. The page listing can be
 * browsed by date etc.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class PageListView extends AbstractView {

    private @Value("${pageList.maxEntries}") int maxEntries;

    private @Resource SearchService searchService;

    /**
     * Lists all blog entries.
     */
    @Framed
    @View(pattern = "/list/index.html", signature = {""})
    @View(pattern = "/list/${date}/index.html", signature = {"date"})
    public String listView(
            @Optional @PathPart("date") DateRange date,
            @Optional @Parameter("p") PaginatorModel paginator,
            ServletRequest req)
    throws ViewException, CillaServiceException {
        FilterModel filter = new FilterModel();
        filter.setDate(date);
        return fetchPages(filter, paginator, req);
    }

    /**
     * Lists all blog entries by category.
     */
    @Framed
    @View(pattern = "/category/${category.id}/${date}/${#simplify(category.name)}.html", signature = {"category", "date"})
    @View(pattern = "/category/${category.id}/${#simplify(category.name)}.html", signature = {"category"})
    public String categoryView(
            @PathPart("category.id") Category category,
            @Optional @PathPart("date") DateRange date,
            @Optional @Parameter("p") PaginatorModel paginator,
            ServletRequest req)
    throws ViewException, CillaServiceException {
        FilterModel filter = new FilterModel();
        filter.setDate(date);
        filter.setCategory(category);
        return fetchPages(filter, paginator, req);
    }

    /**
     * Lists all blog entries by tag.
     */
    @Framed
    @View(pattern = "/tag/${date}/${#encode(tag.name)}.html", signature = {"tag", "date"})
    @View(pattern = "/tag/${#encode(tag.name)}.html", signature = {"tag"})
    public String tagView(
            @PathPart("#encode(tag.name)") Tag tag,
            @Optional @PathPart("date") DateRange date,
            @Optional @Parameter("p") PaginatorModel paginator,
            ServletRequest req)
    throws ViewException, CillaServiceException {
        FilterModel filter = new FilterModel();
        filter.setDate(date);
        filter.setTag(tag);
        return fetchPages(filter, paginator, req);
    }

    /**
     * Lists all blog entries by author.
     */
    @Framed
    @View(pattern = "/author/${author.id}/${date}/${#simplify(author.name)}.html", signature = {"author", "date"})
    @View(pattern = "/author/${author.id}/${#simplify(author.name)}.html", signature = {"author"})
    public String authorView(
            @PathPart("author.id") User user,
            @Optional @PathPart("date") DateRange date,
            @Optional @Parameter("p") PaginatorModel paginator,
            ServletRequest req)
    throws ViewException, CillaServiceException {
        FilterModel filter = new FilterModel();
        filter.setDate(date);
        filter.setCreator(user);
        return fetchPages(filter, paginator, req);
    }

    /**
     * Fetches the pages by query.
     *
     * @param filter
     *            {@link FilterModel} with the query parameters
     * @param paginator
     *            {@link PaginatorModel} to be used, or {@code null} if none was given
     * @param context
     *            {@link ViewFacade} to be used
     */
    private String fetchPages(FilterModel filter, PaginatorModel paginator, ServletRequest req)
    throws CillaServiceException {
        PaginatorModel usePaginator = paginator;
        if (usePaginator == null) {
            usePaginator = new PaginatorModel();
        }
        usePaginator.setPerPage(maxEntries);

        SearchResult result = searchService.search(filter);
        usePaginator.setCount(result.getCount());
        result.setPaginator(usePaginator);

        req.setAttribute("result", result);
        req.setAttribute("paginator", usePaginator);
        return "view/pageList.jsp";
    }

}
