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

import java.util.Collection;

import org.shredzone.cilla.core.model.Authority;

/**
 * DAO for {@link Authority} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface AuthorityDao extends BaseDao<Authority> {

    /**
     * Fetches an {@link Authority} by its name.
     *
     * @param name
     *            {@link Authority} name
     * @return {@link Authority} or {@code null} if not found.
     */
    Authority fetchByName(String name);

    /**
     * Fetches a bulk of {@link Authority} by their name.
     * <p>
     * The returning Collection may have a different order than the order of names that
     * were given. Also the returning Collection could be smaller than the Collection of
     * names when names were not found.
     *
     * @param names
     *            Collection of authority names
     * @return Collection of {@link Authority} that were found.
     */
    Collection<Authority> fetchByNames(Collection<String> names);

}
