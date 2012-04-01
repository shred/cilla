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
package org.shredzone.cilla.core.event;

import java.lang.reflect.Method;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * Tracks the event handler's bean and method, and invokes the handler method.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EventInvoker {

    private Object bean;
    private Method method;

    /**
     * Event handler bean.
     */
    public Object getBean()                 { return bean; }
    public void setBean(Object bean)        { this.bean = bean; }

    /**
     * Event handler method to be invoked.
     */
    public Method getMethod()               { return method; }
    public void setMethod(Method method)    { this.method = method; }

    /**
     * Invoke the event handler.
     *
     * @param event
     *            {@link Event} that was fired
     */
    public void invoke(Event<?> event) {
        Class<?>[] types = method.getParameterTypes();
        Object[] values = new Object[types.length];

        for (int ix = 0; ix < types.length; ix++) {
            values[ix] = evaluateParameter(types[ix], event);
        }

        ReflectionUtils.invokeMethod(method, bean, values);
    }

    /**
     * Evaluates a single parameter of the handler method's parameter list.
     *
     * @param type
     *            Expected parameter type
     * @param event
     *            Event with further data
     * @return Parameter value
     */
    private Object evaluateParameter(Class<?> type, Event<?> event) {
        Object source = event.getSource();
        if (source != null && type.isAssignableFrom(source.getClass())) {
            return type.cast(source);
        }

        if (type.isAssignableFrom(EventType.class)) {
            return event.getType();
        }

        if (type.isAssignableFrom(Event.class)) {
            return event;
        }

        // Alas, we cannot find anything to satisfy this parameter
        throw new IllegalStateException("Unknown parameter type " + type.getName());
    }

}
