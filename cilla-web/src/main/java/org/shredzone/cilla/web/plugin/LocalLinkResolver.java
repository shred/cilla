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
package org.shredzone.cilla.web.plugin;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.web.plugin.annotation.Priority;

/**
 * Resolves local links as they are found in {@code &lt;a href&gt;} attributes.
 * <p>
 * The implementation can be annotated with {@link Priority} in order to change the
 * priority of the matchers.
 *
 * @author Richard "Shred" Körber
 */
public interface LocalLinkResolver {

    /**
     * Resolves a local link in relation to the given {@link Page}.
     *
     * @param url
     *            local link URL to resolve, always relative
     * @param page
     *            {@link Page} this link is related to
     * @param image
     *            {@code true} if the resolved URL should be a reference to an image
     *            resource, {@code false} for a clickable link
     * @return Resolved URL, either relative to the blog or absolute, or {@code null} if
     *         this resolver could not resolve the given link URL
     */
    String resolveLocalLink(String url, Page page, boolean image);

}
