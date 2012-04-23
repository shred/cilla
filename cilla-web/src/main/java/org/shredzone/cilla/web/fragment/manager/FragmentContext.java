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
package org.shredzone.cilla.web.fragment.manager;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

import org.shredzone.cilla.web.fragment.annotation.FragmentValue;
import org.shredzone.commons.view.ViewService;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * An {@link EvaluationContext} for {@link FragmentValue} annotated parameters.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FragmentContext extends StandardEvaluationContext {

    private @Resource ServletContext servletContext;
    private @Resource ViewService viewService;

    private PageContext pageContext;

    /**
     * {@link PageContext} of the invoking JSP.
     */
    public PageContext getPageContext()         { return pageContext; }
    public void setPageContext(PageContext pageContext) { this.pageContext = pageContext; }

    /**
     * The {@link ServletContext}.
     */
    public ServletContext getServletContext()   { return servletContext; }

    /**
     * The {@link ViewService}.
     */
    public ViewService getViewService()         { return viewService; }

    /**
     * Sets an attribute in the Request scope. Can be used to pass values to the
     * {@link #include(String)} template.
     *
     * @param name
     *            Attribute name
     * @param value
     *            Attribute value, or {@code null} to remove this attribute
     */
    public void setAttribute(String name, Object value) {
        pageContext.setAttribute(name, value, PageContext.REQUEST_SCOPE);
    }

    /**
     * Includes a template to the output.
     *
     * @param template
     *            Template name
     */
    public void include(String template) throws IOException {
        try {
            String fullViewPath = viewService.getTemplatePath(template);
            pageContext.include(fullViewPath, false);
        } catch (ServletException ex) {
            throw new IOException("Could not include template " + template, ex);
        }
    }

}
