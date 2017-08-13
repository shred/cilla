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
package org.shredzone.cilla.core.repository;

import java.util.SortedSet;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Tag;

/**
 * DAO for {@link Tag} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface TagDao extends BaseDao<Tag> {

    /**
     * Finds a tag by its name.
     *
     * @param name
     *            Tag name
     * @return {@link Tag}, or {@code null} if none was found
     */
    Tag fetchByName(String name);

    /**
     * Finds a tag by its name, or creates a new tag if there is none.
     *
     * @param name
     *            Tag name
     * @return {@link Tag}
     */
    Tag fetchOrCreate(String name);

    /**
     * Returns a list of all tags that are used at least once.
     *
     * @param pagesOnly
     *            If {@code true}, only the tags used in {@link Page} entities are
     *            collected. If {@code false}, all used tags are collected (also those
     *            used in {@link Picture} entities).
     * @param publicOnly
     *            If {@code true}, only show tags of public content (published, not
     *            expired, not hidden). If {@code false}, also show tabs of unpublished
     *            content.
     * @return List of {@link Tag}, alphabetically sorted. May be empty, but never
     *         {@code null}.
     */
    SortedSet<Tag> fetchAllUsedTags(boolean pagesOnly, boolean publicOnly);

}
