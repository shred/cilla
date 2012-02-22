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
package org.shredzone.cilla.web;

import org.springframework.web.util.HtmlUtils;

/**
 * Contains a meta tag. A meta tag is immutable.
 *
 * @author Richard "Shred" Körber
 */
public final class Meta implements Comparable<Meta> {

    private final String name;
    private final String content;
    private final String scheme;

    /**
     * Creates a new Meta tag.
     *
     * @param name
     *            meta name
     * @param content
     *            content attribute
     */
    public Meta(String name, String content) {
        this(name, content, null);
    }

    /**
     * Creates a new Meta tag.
     *
     * @param name
     *            meta name
     * @param content
     *            content attribute
     * @param scheme
     *            optional scheme attribute, or {@code null}
     */
    public Meta(String name, String content, String scheme) {
        if (name == null || content == null) {
            throw new IllegalArgumentException("name and content must not be null");
        }

        this.name = name;
        this.content = content;
        this.scheme = scheme;
    }

    /**
     * Gets the name attribute.
     */
    public String getName()                 { return name; }

    /**
     * Gets the content attribute.
     */
    public String getContent()              { return content; }

    /**
     * Gets the scheme attribute, or {@code null} if not set.
     */
    public String getScheme()               { return scheme; }

    @Override
    public int compareTo(Meta o) {
        return name.compareTo(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Meta)) {
            return false;
        }
        return name.equals(((Meta) obj).name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Returns the meta tag.
     *
     * @return the meta tag
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<meta name=\"").append(HtmlUtils.htmlEscape(name));
        sb.append("\" content=\"").append(HtmlUtils.htmlEscape(content));
        if (scheme != null) {
           sb.append("\" scheme=\"").append(HtmlUtils.htmlEscape(scheme));
        }
        sb.append("\" />");

        return sb.toString();
    }

}
