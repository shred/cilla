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
package org.shredzone.cilla.ws.cxf;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.annotation.Resource;
import javax.xml.ws.soap.SOAPFaultException;

import org.shredzone.cilla.ws.client.RemoteUserDetails;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.system.LoginWs;
import org.shredzone.cilla.ws.user.UserWs;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * An {@link AuthenticationProvider} for remote authentication.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class CillaRemoteAuthenticationProvider implements AuthenticationProvider {

    private @Resource LoginWs loginWs;
    private @Resource UserWs userWs;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication.getPrincipal() instanceof RemoteUserDetails)) {
            throw new InsufficientAuthenticationException("authentication must contain a RemoteUserDetails principal");
        }

        try {
            RemoteUserDetails userDetails = (RemoteUserDetails) authentication.getPrincipal();

            List<GrantedAuthority> authorities = loginWs.authenticate().getRights().stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(toList());

            userDetails.setAuthorities(authorities);
            userDetails.setUser(userWs.fetchByLogin(userDetails.getUsername()));

            return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        } catch (SOAPFaultException ex) {
            throw new BadCredentialsException("login rejected", ex);
        } catch (CillaServiceException ex) {
            throw new AuthenticationServiceException("couldn't get user details", ex);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
