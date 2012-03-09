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
package org.shredzone.cilla.web.header.tag;

/**
 * A link to a CSS resource.
 *
 * @author Richard "Shred" Körber
 */
public class CssLinkTag extends LinkTag {

    /**
     * Creates a new {@link CssLinkTag}.
     *
     * @param url
     *            URL of the css resource (relative or absolute)
     */
    public CssLinkTag(String url) {
        super("stylesheet", "text/css", url);
    }

    /**
     * Creates a new {@link CssLinkTag}.
     *
     * @param url
     *            URL of the css resource (relative or absolute)
     * @param media
     *            media this css is applied to
     */
    public CssLinkTag(String url, String media) {
        super("stylesheet", "text/css", url, media);
    }

}
