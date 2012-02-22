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
package org.shredzone.cilla.service.impl;

import org.shredzone.cilla.service.SecurityService;
import org.shredzone.cilla.service.security.CillaUserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link SecurityService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    @Override
    public CillaUserDetails getAuthenticatedUser() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (details != null && details instanceof CillaUserDetails) {
            return (CillaUserDetails) details;
        }
        return null;
    }

    @Override
    public boolean hasRole(String role) {
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void requireRole(String role) {
        if (!hasRole(role)) {
            throw new AccessDeniedException(role);
        }
    }

}
