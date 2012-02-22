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

import org.shredzone.cilla.core.model.Header;

/**
 * DAO for {@link Header} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface HeaderDao extends BaseDao<Header> {

    /**
     * Fetches all enabled headers.
     *
     * @return List of all enabled Header. May be empty, but never {@code null}.
     */
    List<Header> fetchEnabled();

    /**
     * Fetches a header by its name. Even unenabled headers are found.
     *
     * @param name
     *            Header name
     * @return Header, or {@code null} if there was no such header.
     */
    Header fetchByName(String name);

    /**
     * Fetches a random single Header out of all enabled headers.
     *
     * @return Random header, or {@code null} if there was no enabled header.
     */
    Header fetchRandomHeader();

}
