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
 * Links to a JavaScript file.
 *
 * @author Richard "Shred" Körber
 */
public class JavaScriptLinkTag implements HeadTag {

    private final String src;

    /**
     * Creates a new {@link JavaScriptLinkTag}.
     *
     * @param src
     *            URL of the JavaScript resource (relative or absolute)
     */
    public JavaScriptLinkTag(String src) {
        this.src = src;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\" src=\"").append(src).append("\"></script>");
        return sb.toString();
    }

}
