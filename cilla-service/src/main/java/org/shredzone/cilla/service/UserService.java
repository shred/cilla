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

import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * A service for {@link User} operations.
 *
 * @author Richard "Shred" Körber
 */
public interface UserService {

    /**
     * Creates a new, initialized {@link User} entity.
     *
     * @return Created {@link User}
     */
    User createNew();

    /**
     * Updates a {@link User}. It is checked if the authenticated user is allowed to
     * change this {@link User}.
     *
     * @param user
     *            {@link User} to update
     */
    void updateUser(User user) throws CillaServiceException;

    /**
     * Deletes a {@link User}. It is checked if the authenticated user is allowed to
     * delete this {@link User}.
     *
     * @param user
     *            {@link User} to be deleted
     */
    void deleteUser(User user);

    /**
     * Changes the login name of a user.
     *
     * @param user
     *            {@link User} to change
     * @param newLogin
     *            new login
     */
    void changeLogin(User user, String newLogin) throws CillaServiceException;

    /**
     * Changes the password of a user. The user's current password must be passed in for
     * security reasons.
     *
     * @param user
     *            {@link User}
     * @param oldPwd
     *            Current password
     * @param newPwd
     *            New password
     */
    void changePassword(User user, String oldPwd, String newPwd) throws CillaServiceException;

    /**
     * Changes the role of a user.
     *
     * @param user
     *            {@link User} to change
     * @param role
     *            new {@link Role}
     */
    void changeRole(User user, Role role) throws CillaServiceException;

}
