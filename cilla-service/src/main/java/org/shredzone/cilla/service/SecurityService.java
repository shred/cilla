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

import org.shredzone.cilla.service.security.CillaUserDetails;

/**
 * Service for security and access restriction.
 *
 * @author Richard "Shred" Körber
 */
public interface SecurityService {

    /**
     * Gets the currently authenticated user.
     *
     * @return the user currently logged in, or {@code null} if nobody is logged in
     */
    CillaUserDetails getAuthenticatedUser();

    /**
     * Checks if the current user has the given role.
     *
     * @param role
     *            name of the role to be checked
     * @return {@code true} if the current user has the role, {@code false} otherwise
     */
    boolean hasRole(String role);

    /**
     * Makes sure that the current user has the given role. Throws an exception if the
     * user does not have sufficient rights.
     *
     * @param role
     *            name of the role that is required
     */
    void requireRole(String role);

}
