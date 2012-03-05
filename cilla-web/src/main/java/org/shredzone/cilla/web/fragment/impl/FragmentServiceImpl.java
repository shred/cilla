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
package org.shredzone.cilla.web.fragment.impl;

import javax.annotation.Resource;
import javax.servlet.jsp.PageContext;

import org.shredzone.cilla.web.fragment.FragmentService;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;
import org.shredzone.cilla.web.fragment.manager.FragmentInvoker;
import org.shredzone.cilla.web.fragment.manager.FragmentManager;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link FragmentService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
public class FragmentServiceImpl implements FragmentService {

    private @Resource ApplicationContext applicationContext;
    private @Resource FragmentManager fragmentManager;

    @Override
    public FragmentContext createContext(PageContext pageContext) throws CillaServiceException {
        FragmentContext fc = applicationContext.getBean(FragmentContext.class);
        fc.setPageContext(pageContext);
        return fc;
    }

    @Override
    public String renderFragment(String name, FragmentContext context) throws CillaServiceException {
        FragmentInvoker invoker = fragmentManager.getInvoker(name);
        if (invoker == null) {
            throw new CillaServiceException("Fragment '" + name + "' not found.");
        }

        Object result = invoker.invoke(context);

        if (result == null) {
            return null;
        }

        if (result instanceof CharSequence) {
            return result.toString();
        }

        throw new CillaServiceException("Fragment renderer returned " + result.getClass().getName());
    }

}
