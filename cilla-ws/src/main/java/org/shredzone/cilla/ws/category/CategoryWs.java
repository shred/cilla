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
package org.shredzone.cilla.ws.category;

import java.util.List;

import javax.jws.WebService;

import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Services for Category.
 *
 * @author Richard "Shred" Körber
 */
@WebService
public interface CategoryWs {

    /**
     * Fetches a {@link CategoryDto} by its ID.
     *
     * @param id
     *            Category id
     * @return {@link CategoryDto}, or {@code null} if it does not exist
     */
    CategoryDto fetch(long id) throws CillaServiceException;

    /**
     * Returns a list of all {@link CategoryDto}, ordered by name.
     */
    List<CategoryDto> list() throws CillaServiceException;

    /**
     * Creates a new {@link CategoryDto}.
     */
    CategoryDto createNew() throws CillaServiceException;

}
