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
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.InterceptorChain;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.stereotype.Component;

/**
 * SOAP interceptor for incoming authentication headers.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class SecurityInInterceptor extends AbstractSoapInterceptor {

    private @Resource CillaUsernameTokenValidator cillaUsernameTokenValidator;

    private WSS4JInInterceptor wss4jInInterceptor;
    private SAAJInInterceptor saajInInterceptor;

    public SecurityInInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    @PostConstruct
    protected void setup() {
        Map<QName, Object> validator = new HashMap<>();
        validator.put(WSConstants.USERNAME_TOKEN, cillaUsernameTokenValidator);

        Map<String, Object> properties = new HashMap<>();
        properties.put(WSHandlerConstants.ACTION, WSConstants.USERNAME_TOKEN_LN);
        properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
        properties.put(WSS4JInInterceptor.VALIDATOR_MAP, validator);
        wss4jInInterceptor = new WSS4JInInterceptor(properties);

        saajInInterceptor = new SAAJInInterceptor();
    }

    @Override
    public Set<QName> getUnderstoodHeaders() {
        return wss4jInInterceptor.getUnderstoodHeaders();
    }

    @Override
    public void handleMessage(SoapMessage message) throws Fault {
        InterceptorChain chain = message.getInterceptorChain();
        chain.add(saajInInterceptor);
        chain.add(wss4jInInterceptor);
    }

}
