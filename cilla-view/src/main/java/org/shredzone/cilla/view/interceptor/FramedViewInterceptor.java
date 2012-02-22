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

import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.repository.CategoryDao;
import org.shredzone.cilla.core.repository.HeaderDao;
import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.commons.view.EmptyViewInterceptor;
import org.shredzone.commons.view.ViewContext;
import org.shredzone.commons.view.exception.ViewException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Interceptor that prepares {@link Framed} annotated views.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class FramedViewInterceptor extends EmptyViewInterceptor {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource CategoryDao categoryDao;
    private @Resource HeaderDao headerDao;

    @Override
    public void onViewHandlerInvocation(ViewContext context, Object bean, Method method) {
        if (method.isAnnotationPresent(Framed.class)) {
            try {
                HttpServletRequest req = context.getValueOfType(HttpServletRequest.class);
                HttpServletResponse resp = context.getValueOfType(HttpServletResponse.class);
                setupFrame(req, resp);
            } catch (ViewException ex) {
                // Should not happen. Just log the exception and do not set up the frame.
                log.error("No HTTP request", ex);
            }
        }
    }

    /**
     * Sets up generic attributes common to all views using a standard frame.
     */
    public void setupFrame(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("headerImage", headerDao.fetchRandomHeader());
        req.setAttribute("rootCategories", categoryDao.fetchRootCategories());
    }

}
