/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2024 Richard "Shred" Körber
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

import org.springframework.web.util.HtmlUtils;

/**
 * Contains a RDFa style meta tag.
 * <p>
 * This is a {@link MetaTag} where the {@code property} attribute is used instead of
 * {@code name}.
 * <p>
 * A meta tag is immutable.
 *
 * @see <a href="https://en.wikipedia.org/wiki/RDFa">RDFa</a>
 */
public class RDFaMetaTag extends MetaTag {

    /**
     * Creates a new RDFa Meta tag.
     *
     * @param property
     *            meta property
     * @param content
     *            content attribute
     */
    public RDFaMetaTag(String property, String content) {
        super(property, content);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<meta property=\"").append(HtmlUtils.htmlEscape(getName()));
        sb.append("\" content=\"").append(HtmlUtils.htmlEscape(getContent()));
        if (getScheme() != null) {
            sb.append("\" scheme=\"").append(HtmlUtils.htmlEscape(getScheme()));
        }
        sb.append("\" />");

        return sb.toString();
    }

}
