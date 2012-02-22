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

/**
 * Enumeration of available feed types.
 *
 * @author Richard "Shred" Körber
 */
public enum FeedType {
    RSS("rss", "application/rss+xml", "rss_1.0"),
    RSS2("rss2", "application/rss+xml", "rss_2.0"),
    ATOM("xml", "application/atom+xml", "atom_1.0");

    private final String suffix;
    private final String contentType;
    private final String type;

    private FeedType(String suffix, String contentType, String type) {
        this.suffix = suffix;
        this.contentType = contentType;
        this.type = type;
    }

    /**
     * Returns the suffix of the feed file.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Returns the content type of the feed.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the ROME type of the feed.
     */
    public String getType() {
        return type;
    }

}
