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

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.shredzone.cilla.ws.client.RemoteUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Java security callback handler for authentication.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class SecurityContextCallback implements CallbackHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            WSPasswordCallback pc = (WSPasswordCallback) callback;
            if (pc.getUsage() == WSPasswordCallback.USERNAME_TOKEN) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof RemoteUserDetails) {
                    RemoteUserDetails rud = (RemoteUserDetails) auth.getPrincipal();
                    pc.setIdentifier(rud.getUsername());
                    pc.setPassword(rud.getPassword());
                    log.debug("Sent credentials for user {}", rud.getUsername());
                } else {
                    log.error("No useable authentication found");
                }
            }
        }
    }

}
