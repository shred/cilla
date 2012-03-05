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
package org.shredzone.cilla.web.tag;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.web.fragment.FragmentService;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.jshred.spring.taglib.annotation.Tag;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Renders a fragment.
 *
 * @author Richard "Shred" Körber
 */
@Tag(type = BodyTag.class)
@TagInfo("Renders a fragment.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RenderTag extends BodyTagSupport implements Parameterizable {
    private static final long serialVersionUID = -23348027985991513L;

    private @TagParameter(required = true) String fragment;
    private @TagParameter Object item;
    private @TagParameter Boolean rendered;

    private @Resource FragmentService fragmentService;

    private FragmentContext context;

    @Override
    public int doStartTag() throws JspException {
        try {
            context = fragmentService.createContext(pageContext);
            context.setRootObject(item);
        } catch (CillaServiceException ex) {
            throw new JspException("Could not generate FragmentContext", ex);
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        if (rendered != null && rendered == Boolean.FALSE) {
            return EVAL_PAGE;
        }

        String result;
        try {
            result = fragmentService.renderFragment(fragment, context);
            if (result != null) {
                pageContext.getOut().print(result);
            }
        } catch (IOException ex) {
            throw new JspException("Failed to render fragment " + fragment, ex);
        } catch (CillaServiceException ex) {
            throw new JspException("Failed to render fragment " + fragment, ex);
        }

        context = null;

        return EVAL_PAGE;
    }

    @Override
    public void addParam(String name, Object value) {
        context.setVariable(name, value);
    }

}
