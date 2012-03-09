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
 * A css tag containing style format rules.
 *
 * @author Richard "Shred" Körber
 */
public class CssTag implements HeadTag {

    private final String media;
    private final StringBuilder body = new StringBuilder();

    /**
     * Creates a {@link CssTag}.
     */
    public CssTag() {
        media = null;
    }

    /**
     * Creates a {@link CssTag}.
     *
     * @param media
     *            media this css is applied to
     */
    public CssTag(String media) {
        this.media = media;
    }

    /**
     * Appends one or more formatting rules to the CSS.
     *
     * @param format
     *            CSS formatting rule
     * @return itself
     */
    public CssTag append(String format) {
        body.append(format);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<style type=\"text/css\"");

        if (media != null) {
            sb.append(" media=\"").append(media).append('"');
        }

        sb.append(">\n").append(body).append("</style>");

        return sb.toString();
    }

}
