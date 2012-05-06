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

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.util.DateUtils;
import org.shredzone.cilla.service.PageService;
import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.service.search.SearchService;
import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.cilla.view.model.PageTurnerModel;
import org.shredzone.cilla.web.comment.CommentFormHandler;
import org.shredzone.cilla.ws.PageOrder;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Views for showing a single page.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class PageView extends AbstractView {

    private @Value("${page.order}") PageOrder pageOrder;

    private @Resource PageService pageService;
    private @Resource PageDao pageDao;
    private @Resource SearchService searchService;
    private @Resource CommentFormHandler commentFormHandler;

    /**
     * Renders a page.
     */
    @Framed
    @View(pattern = "/page/${page.id}/${#simplify(page.title)}.html", signature = {"page"})
    public String pageView(
            @PathPart("page.id") Page page,
            HttpServletRequest req,
            HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        return renderPage(page, req, resp);
    }

    /**
     * Renders a page by its name.
     */
    @Framed
    @View(pattern = "/named/${#pagename}.html", signature = {"#pagename"})
    public String pageByNameView(
            @PathPart("#pagename") Page page,
            HttpServletRequest req,
            HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        return renderPage(page, req, resp);
    }

    /**
     * Page renderer.
     *
     * @param page
     *            {@link Page} to be rendered
     * @param req
     *            {@link HttpServletRequest}
     * @return view to render
     */
    private String renderPage(Page page, HttpServletRequest req, HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        if (!pageService.isVisible(page)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (handleRestricted(page, req)) {
            req.setAttribute("page", page);
            req.setAttribute("turner", new PageTurnerModel(pageDao.fetchPreviousPage(page), pageDao.fetchNextPage(page)));
            return "view/pageUnlock.jsp";
        }

        addHeadersIfRestricted(page, req, resp);

        commentFormHandler.handleComment(page, req);

        List<Page> sameSubject = pageDao.fetchSameSubject(page);
        if (sameSubject != null && sameSubject.size() > 1) {
            // only set if there is at least a second page with the same subject
            req.setAttribute("sameSubject", sameSubject);
        }

        DateRange dr = new DateRange();
        Date orderDate = DateUtils.getOrderDate(page, pageOrder);
        if (orderDate != null) {
            dr.setDate(orderDate);
        }

        FilterModel filter = new FilterModel();
        filter.setDate(dr);
        SearchResult result = searchService.search(filter);
        req.setAttribute("result", result);

        req.setAttribute("page", page);
        req.setAttribute("turner", new PageTurnerModel(pageDao.fetchPreviousPage(page), pageDao.fetchNextPage(page)));

        return "view/page.jsp";
    }

}
