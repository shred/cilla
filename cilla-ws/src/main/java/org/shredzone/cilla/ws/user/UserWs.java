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
package org.shredzone.cilla.ws.user;

import java.util.List;

import javax.jws.WebService;

import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Service for managing users.
 *
 * @author Richard "Shred" Körber
 */
@WebService
public interface UserWs {

    /**
     * Fetches a {@link UserDto} by its login name.
     *
     * @param login
     *            Login name
     * @return {@link UserDto}, or {@code null} if there is no such user
     */
    UserDto fetchByLogin(String login) throws CillaServiceException;

    /**
     * Fetches a {@link UserDto} by its ID.
     *
     * @param id
     *            User ID
     * @return {@link UserDto}, or {@code null} if it does not exist
     */
    UserDto fetch(long id) throws CillaServiceException;

    /**
     * Counts the number of all users.
     */
    long count();

    /**
     * Lists all {@link UserDto} matching the criteria.
     *
     * @param criteria
     *            {@link ListRange}, or {@code null} for all
     * @return List of matching {@link UserDto}
     */
    List<UserDto> list(ListRange criteria);

    /**
     * Creates a new {@link UserDto}.
     *
     * @return {@link UserDto} that was created
     */
    UserDto createNew() throws CillaServiceException;

}
