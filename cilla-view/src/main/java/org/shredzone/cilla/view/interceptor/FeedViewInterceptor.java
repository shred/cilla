/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2018 Richard "Shred" Körber
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
package org.shredzone.cilla.view.interceptor;

import org.shredzone.cilla.core.model.Page;

import com.rometools.rome.feed.synd.SyndEntry;

/**
 * Interceptor for feed generation.
 *
 * @author Richard "Shred" Körber
 */
public interface FeedViewInterceptor {

    /**
     * Returns {@code true} if a {@link Page} is to be ignored for the feed output.
     *
     * @param page
     *            Current {@link Page} to be rendered
     * @return {@code true}: ignore this page, {@code false}: use this page in the feed
     *         output.
     */
    default boolean isIgnored(Page page) {
        return false;
    }

    /**
     * Filters the description text of a {@link Page} that is to be used in the entry.
     *
     * @param page
     *            Current {@link Page} that is being rendered in the feed
     * @param description
     *            Description HTML for the feed
     * @return Filtered HTML
     */
    default String filterDescription(Page page, String description) {
        return description;
    }

    /**
     * Post-processes the {@link SyndEntry} before it is used in the feed output.
     *
     * @param page
     *            Current {@link Page} that is being rendered in the feed
     * @param entry
     *            {@link SyndEntry} being generated
     */
    default void postProcessEntry(Page page, SyndEntry entry) {
        // does nothing by default
    }

}
