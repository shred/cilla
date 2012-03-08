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
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspWriter;

import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentItem;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.web.fragment.annotation.FragmentValue;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.ViewService;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.ReflectionUtils;

/**
 * Invokes a fragment renderer.
 * <p>
 * Objects are immutable.
 *
 * @author Richard "Shred" Körber
 */
public final class FragmentInvoker {
    private final Object bean;
    private final Method method;
    private final ConversionService conversionService;
    private final Expression[] expressions;
    private final boolean[] items;
    private final String template;

    /**
     * Creates a new {@link FragmentInvoker}.
     *
     * @param bean
     *            target Spring bean to be invoked
     * @param method
     *            target method to be invoked
     * @param template
     *            JSP template to be used for rendering, or {@code null} if none is
     *            defined
     * @param conversionService
     *            {@link ConversionService} to be used for converting
     */
    public FragmentInvoker(Object bean, Method method, String template, ConversionService conversionService) {
        this.bean = bean;
        this.method = method;
        this.template = template;
        this.conversionService = conversionService;

        Annotation[][] annotations = method.getParameterAnnotations();
        expressions = new Expression[annotations.length];
        items = new boolean[annotations.length];

        ExpressionParser parser = new SpelExpressionParser();

        for (int ix = 0; ix < annotations.length; ix++) {
            for (Annotation sub : annotations[ix]) {
                if (sub instanceof FragmentValue) {
                    expressions[ix] = parser.parseExpression(((FragmentValue) sub).value());
                } else if (sub instanceof FragmentItem) {
                    items[ix] = true;
                }
            }
        }
    }

    /**
     * The Spring bean that was annotated with {@link FragmentRenderer}.
     */
    public Object getBean() { return bean; }

    /**
     * The target method that was annotated with {@link Fragment}.
     */
    public Method getMethod() { return method; }

    /**
     * The JSP template that was defined with the {@link Fragment}, or {@code null} if none
     * was defined.
     */
    public String getTemplate() { return template; }

    /**
     * Invokes the fragment renderer.
     *
     * @param context
     *            {@link FragmentContext} containing all necessary data for invoking
     * @return Fragment string returned by the renderer. May be {@code null}.
     */
    public String invoke(FragmentContext context) throws CillaServiceException {
        if (context == null) {
            throw new NullPointerException("context must not be null");
        }

        Class<?>[] types = method.getParameterTypes();
        Object[] values = new Object[types.length];

        for (int ix = 0; ix < types.length; ix++) {
            values[ix] = evaluateParameter(types[ix], ix, context);
        }

        Object html = ReflectionUtils.invokeMethod(method, bean, values);

        if (html != null && html instanceof CharSequence) {
            // Method returned a char sequence
            return html.toString();
        }

        if (template != null) {
            // A JSP template is defined for rendering
            context.include(template);
        }

        // Renderer returned nothing, it may have written its content itself
        return null;
    }

    /**
     * Evaluates a single parameter of the renderer's parameter list.
     *
     * @param type
     *            Expected parameter type
     * @param ix
     *            Parameter index
     * @param context
     *            {@link FragmentContext} containing all necessary data for evaluation
     * @return Parameter value to be passed to the method
     */
    private Object evaluateParameter(Class<?> type, int ix, FragmentContext context)
    throws CillaServiceException {
        if (items[ix]) {
            return conversionService.convert(context.getRootObject().getValue(), type);

        } else if (expressions[ix] != null) {
            return expressions[ix].getValue(context, type);

        } else if (   context.getRootObject() != null
                   && context.getRootObject().getTypeDescriptor() != null
                   && context.getRootObject().getTypeDescriptor().getType().isAssignableFrom(type)) {
            return context.getRootObject().getTypeDescriptor().getType().cast(
                context.getRootObject().getValue()
            );

        } else if (ServletRequest.class.isAssignableFrom(type)) {
            return context.getPageContext().getRequest();

        } else if (ServletResponse.class.isAssignableFrom(type)) {
            return context.getPageContext().getResponse();

        } else if (Locale.class.isAssignableFrom(type)) {
            return context.getPageContext().getRequest().getLocale();

        } else if (ServletContext.class.isAssignableFrom(type)) {
            return context.getServletContext();

        } else if (ViewService.class.isAssignableFrom(type)) {
            return context.getViewService();

        } else if (JspWriter.class.isAssignableFrom(type)) {
            return context.getPageContext().getOut();

        } else if (PrintWriter.class.isAssignableFrom(type)) {
            try {
                return context.getPageContext().getResponse().getWriter();
            } catch (IOException ex) {
                throw new CillaServiceException("Could not get http response writer", ex);
            }

        } else if (EvaluationContext.class.isAssignableFrom(type)) {
            return context;

        } else {
            throw new IllegalArgumentException("Parameter " + ix + " has no matching value for type " + type.getName());
        }
    }

}
