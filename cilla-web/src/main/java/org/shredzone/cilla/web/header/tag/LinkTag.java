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

import org.springframework.web.util.HtmlUtils;

/**
 * A generic link to a resource.
 *
 * @author Richard "Shred" Körber
 */
public class LinkTag implements HeadTag {

    private final String rel;
    private final String type;
    private final String href;
    private final String media;

    /**
     * Creates a new Link tag.
     *
     * @param rel
     *            link relationship
     * @param type
     *            content type of the linked resource
     * @param href
     *            url of the linked resource (relative or absolute)
     */
    public LinkTag(String rel, String type, String href) {
        this(rel, type, href, null);
    }

    /**
     * Creates a new Link tag.
     *
     * @param rel
     *            link relationship
     * @param type
     *            content type of the linked resource
     * @param href
     *            url of the linked resource (relative or absolute)
     * @param media
     *            media this link is applied to
     */
    public LinkTag(String rel, String type, String href, String media) {
        this.rel = rel;
        this.type = type;
        this.href = href;
        this.media = media;
    }

    /**
     * Returns the link relationship.
     */
    public String getRel() {
        return rel;
    }

    /**
     * Returns the content type of the resource.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the link url.
     */
    public String getHref() {
        return href;
    }

    /**
     * Returns the media this link applies to.
     */
    public String getMedia() {
        return media;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("<link");

        if (rel != null) {
            sb.append(" rel=\"").append(HtmlUtils.htmlEscape(rel)).append('"');
        }

        if (type != null) {
            sb.append(" type=\"").append(HtmlUtils.htmlEscape(type)).append('"');
        }

        if (href != null) {
            sb.append(" href=\"").append(HtmlUtils.htmlEscape(href)).append('"');
        }

        if (media != null) {
            sb.append(" media=\"").append(HtmlUtils.htmlEscape(media)).append('"');
        }

        sb.append(" />");

        return sb.toString();
    }

}
