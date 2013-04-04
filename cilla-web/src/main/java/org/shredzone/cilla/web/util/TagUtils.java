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
package org.shredzone.cilla.web.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

/**
 * Utility methods for tag handling.
 *
 * @author Richard "Shred" Körber
 */
public final class TagUtils {

    private TagUtils() {
        // Utility class without constructor
    }

    /**
     * Set an attribute with the given scope.
     *
     * @param pageContext
     *            {@link PageContext} of the invoking tag
     * @param attribute
     *            Attribute name
     * @param value
     *            Value of that attribute
     * @param scope
     *            Scope to be used. {@code null} means page scope.
     * @throws JspException
     *             if the scope was unknown
     */
    public static void setScopedAttribute(PageContext pageContext, String attribute, Object value, String scope)
    throws JspException {
        int scopeId = PageContext.PAGE_SCOPE;
        if (scope != null) {
            switch (scope) {
                case "page":        scopeId = PageContext.PAGE_SCOPE;        break;
                case "request":     scopeId = PageContext.REQUEST_SCOPE;     break;
                case "session":     scopeId = PageContext.SESSION_SCOPE;     break;
                case "application": scopeId = PageContext.APPLICATION_SCOPE; break;
                default: throw new JspException("Unknown scope: " + scope);
            }
        }
        pageContext.setAttribute(attribute, value, scopeId);
    }

}
