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

import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.model.User;

/**
 * DAO for {@link User} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface UserDao extends BaseDao<User> {

    /**
     * Fetches a {@link User} by the given login.
     *
     * @param login
     *            Login name
     * @return {@link User} that matches the name, or {@code null} if no such user was
     *         found
     */
    User fetchByLogin(String login);

    /**
     * Fetches all users with the given role.
     *
     * @param role
     *            {@link Role} to fetch all {@link User} for
     * @return Collection of {@link User} with that role
     */
    Collection<User> fetchAllWithRole(Role role);

    /**
     * Fetches all users with the given authority.
     *
     * @param authority
     *            Authority to fetch all {@link User} for
     * @return Collection of {@link User} with that authority
     */
    Collection<User> fetchAllWithAuthority(String authority);

}
