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

import javax.annotation.Resource;

import org.apache.cxf.interceptor.security.AuthenticationException;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.message.token.UsernameToken;
import org.apache.ws.security.validate.UsernameTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * A {@link UsernameTokenValidator} that validates against the Cilla authentication
 * manager.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class CillaUsernameTokenValidator extends UsernameTokenValidator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource AuthenticationManager authenticationManager;

    @Override
    protected void verifyPlaintextPassword(UsernameToken usernameToken, RequestData data)
    throws WSSecurityException {
        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                            usernameToken.getName(), usernameToken.getPassword());
            auth = authenticationManager.authenticate(auth);
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Successfully authenticated user {}", usernameToken.getName());
        } catch (AuthenticationException ex) {
            log.error("Unable to authenticate user {}", usernameToken.getName(), ex);
            throw new WSSecurityException(WSSecurityException.FAILED_AUTHENTICATION);
        }
    }

}
