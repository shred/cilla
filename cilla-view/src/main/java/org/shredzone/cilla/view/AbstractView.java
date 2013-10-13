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

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.service.PageService;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.web.page.ResourceLockManager;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * An abstract view superclass that offers common methods.
 *
 * @author Richard "Shred" Körber
 */
@Component
public abstract class AbstractView {

    private @Resource PageService pageService;
    private @Resource ResourceLockManager unlockService;
    private @Resource LinkService linkService;

    /**
     * Handles page restrictions.
     *
     * @param page
     *            {@link Page} to check for restrictions
     * @param req
     *            {@link HttpServletRequest}
     * @return if {@code true}, the page is restricted and needs to be unlocked. If
     *         {@code false}, the page either not restricted, or restricted and unlocked.
     */
    protected boolean handleRestricted(Page page, HttpServletRequest req) {
        // Is the page restricted anyways?
        if (!page.isRestricted()) return false;

        // Is the page restricted, but already unlocked?
        if (unlockService.isUnlocked(req.getSession(), page)) return false;

        // Is there an answer parameter?
        String response = req.getParameter("restrictionresponse");
        if (response != null) {
            if (pageService.isAcceptedResponse(page, response)) {
                // Valid response! Unlock the page and show it.
                unlockService.unlockStore(req.getSession(), page);
                return false;
            }

            req.setAttribute("badRestrictionResponse", true);
        }

        return true;
    }

    /**
     * If the page is restricted and unlocked, send headers so public caches won't cache
     * the page, but private caches (user-agent) do.
     *
     * @param page
     *            {@link Page} to check
     * @param req
     *            {@link HttpServletRequest} request to be checked
     * @param resp
     *            {@link HttpServletResponse} response to add headers to
     */
    protected void addHeadersIfRestricted(Page page, HttpServletRequest req, HttpServletResponse resp) {
        if (page.isRestricted() && unlockService.isUnlocked(req.getSession(), page)) {
            // Page is restricted and unlocked
            resp.setDateHeader("Expires", 0L);
            resp.setHeader("Cache-Control", "s-max-age=0, private");
        }
    }

    /**
     * Redirect to the section's page main view when the page is restricted and has not
     * been unlocked yet. If a view of a locked page has been invoked, the visitor will be
     * redirected to the page where the unlock question is asked.
     *
     * @param page
     *            {@link Page} that is to be shown
     * @param req
     *            {@link HttpServletRequest}
     * @param resp
     *            {@link HttpServletResponse}
     * @return if {@code true}, the page access is restricted and must not be shown to the
     *         user. A redirection to the main page has already been placed.
     */
    protected boolean redirectRestricted(Page page, HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        if (page.isRestricted() && !unlockService.isUnlocked(req.getSession(), page)) {
            try {
                resp.sendRedirect(linkService.linkTo().page(page).toString());
                return true;
            } catch (IOException ex) {
                throw new ViewException(ex);
            }
        }

        return false;
    }

    /**
     * Checks that the request sent an "If-Modified-Since" header, and the date matches
     * the given date.
     *
     * @param req
     *            {@link HttpServletRequest} to use
     * @param date
     *            {@link Date} to check
     * @return {@code true}: Resource has not changed since it was last delivered
     */
    protected boolean isNotModifiedSince(HttpServletRequest req, Date date) {
        long modifiedSinceTs = -1;
        try {
            modifiedSinceTs = req.getDateHeader("If-Modified-Since");
        } catch (IllegalArgumentException ex) {
            // As stated in RFC2616 Sec. 14.25, an invalid date will just be ignored.
        }

        return (modifiedSinceTs >= 0 && (modifiedSinceTs / 1000) == (date.getTime() / 1000));
    }

    /**
     * Streams a {@link ResourceDataSource}. Also cares about setting proper HTTP response
     * headers.
     *
     * @param ds
     *            {@link ResourceDataSource} to stream
     * @param req
     *            {@link HttpServletRequest}
     * @param resp
     *            {@link HttpServletResponse}
     */
    protected void streamDataSource(ResourceDataSource ds, HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        try {
            if (isNotModifiedSince(req, ds.getLastModified())) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_MODIFIED);
            }

            Long length = ds.getLength();
            if (length != null && length <= Integer.MAX_VALUE) {
                // Converting long to int is safe here...
                resp.setContentLength(length.intValue());
            }

            resp.setContentType(ds.getContentType());
            resp.setDateHeader("Last-Modified", ds.getLastModified().getTime());

            try (InputStream in = ds.getInputStream()) {
                FileCopyUtils.copy(in, resp.getOutputStream());
            }
        } catch (IOException ex) {
            throw new ViewException(ex);
        }
    }

}
