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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJOutInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.stereotype.Component;

/**
 * An OutInterceptor that handles a simple UsernameToken authentication using Spring
 * Security.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class SecurityOutInterceptor extends AbstractSoapInterceptor {

    private @Resource SecurityContextCallback securityContextCallback;

    private WSS4JOutInterceptor wss4jOutInterceptor;
    private SAAJOutInterceptor saajOutInterceptor;

    public SecurityOutInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @PostConstruct
    public void setup() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(WSHandlerConstants.ACTION, WSConstants.USERNAME_TOKEN_LN);
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        // User will be replaced by the real user in the callback
        properties.put(WSHandlerConstants.USER, "anonymous");
        properties.put(WSHandlerConstants.PW_CALLBACK_REF, securityContextCallback);
        wss4jOutInterceptor = new WSS4JOutInterceptor(properties);

        saajOutInterceptor = new SAAJOutInterceptor();
    }

    @Override
    public Set<QName> getUnderstoodHeaders() {
        return wss4jOutInterceptor.getUnderstoodHeaders();
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        InterceptorChain chain = message.getInterceptorChain();
        chain.add(saajOutInterceptor);
        chain.add(wss4jOutInterceptor);
    }

}
