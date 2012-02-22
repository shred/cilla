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

import java.util.List;

import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Page;

/**
 * DAO for {@link Medium} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface MediumDao extends BaseDao<Medium> {

    /**
     * Counts all {@link Medium} belonging to a {@link Page}.
     *
     * @param page
     *            Page the query is related to
     * @return Number of media
     */
    long countAll(Page page);

    /**
     * Fetches all {@link Medium} belonging to a {@link Page}.
     *
     * @param page
     *            Page the query is related to
     * @return List of {@link Medium}. May be empty, but is never {@code null}.
     */
    List<Medium> fetchAll(Page page);

    /**
     * Fetches a {@link Medium} entity by its bucket name and name.
     *
     * @param page
     *            Page the query is related to
     * @param name
     *            Media name
     * @return {@link Medium} or {@code null} if there is no such medium
     */
    Medium fetchByName(Page page, String name);

}
