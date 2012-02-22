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
package org.shredzone.cilla.service;

import java.util.List;
import java.util.Map;

import org.shredzone.cilla.core.model.Tag;

/**
 * Service for handling tags.
 *
 * @author Richard "Shred" Körber
 */
public interface TagService {

    /**
     * Proposes tags by their tag name.
     *
     * @param query
     *            Query to find similar tag names for.
     * @param limit
     *            Maximum number of tags to be returned
     * @return List of tag strings
     */
    List<String> proposeTags(String query, int limit);

    /**
     * Computes tag cloud data. Each tag is weighted by the number of pages they tag, and
     * the age of that page. Unpublished pages and pages older than the given age limit
     * are not taken into account.
     * <p>
     * The result is cached.
     *
     * @param limit
     *            age limit (in ms)
     * @return Map of tags in the cloud, and a weight between 0 and 1 (inclusive).
     */
    Map<Tag, Float> createTagCloud(long limit);

}
