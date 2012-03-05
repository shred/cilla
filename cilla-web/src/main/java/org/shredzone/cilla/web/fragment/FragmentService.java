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
package org.shredzone.cilla.web.fragment;

import javax.servlet.jsp.PageContext;

import org.shredzone.cilla.web.fragment.manager.FragmentContext;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * A service that renders HTML fragments.
 *
 * @author Richard "Shred" Körber
 */
public interface FragmentService {

    /**
     * Creates a new {@link FragmentContext}.
     *
     * @param pageContext
     *            {@link PageContext} of the invoking page
     * @return {@link FragmentContext} that was generated
     */
    FragmentContext createContext(PageContext pageContext) throws CillaServiceException;

    /**
     * Renders a fragment.
     *
     * @param name
     *            Fragment name
     * @param context
     *            {@link FragmentContext} with further data, may be {@code null}
     * @return HTML of the rendered fragment
     */
    String renderFragment(String name, FragmentContext context) throws CillaServiceException;

}
