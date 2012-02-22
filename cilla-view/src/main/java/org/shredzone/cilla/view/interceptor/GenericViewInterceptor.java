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
package org.shredzone.cilla.view.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.commons.view.EmptyViewInterceptor;
import org.springframework.stereotype.Component;

/**
 * A generic view interceptor that sets common headers and attributes.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class GenericViewInterceptor extends EmptyViewInterceptor {

    @Override
    public void onRequest(HttpServletRequest req, HttpServletResponse resp) {
        Date now = new Date();
        req.setAttribute("now", now);
        resp.setDateHeader("Date", now.getTime());
    }

    @Override
    public String onRendering(String template, HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("selfUri", req.getRequestURI());
        resp.setHeader("Vary", "Accept-Language");
        return null;
    }

}
