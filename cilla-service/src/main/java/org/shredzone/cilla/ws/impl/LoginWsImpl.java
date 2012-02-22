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
package org.shredzone.cilla.ws.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.shredzone.cilla.service.SecurityService;
import org.shredzone.cilla.service.security.CillaUserDetails;
import org.shredzone.cilla.ws.system.GrantedRoleDto;
import org.shredzone.cilla.ws.system.LoginWs;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link LoginWs} implementation.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "LoginWs",
    endpointInterface = "org.shredzone.cilla.ws.system.LoginWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class LoginWsImpl implements LoginWs {

    private @Resource SecurityService securityService;

    @Override
    public GrantedRoleDto authenticate() {
        CillaUserDetails cud = securityService.getAuthenticatedUser();

        GrantedRoleDto dto = new GrantedRoleDto();
        dto.setName(cud.getRole());

        Set<String> rights = new HashSet<String>();
        for (GrantedAuthority authority : cud.getAuthorities()) {
            rights.add(authority.getAuthority());
        }
        dto.setRights(Collections.unmodifiableSet(rights));

        return dto;
    }

}
