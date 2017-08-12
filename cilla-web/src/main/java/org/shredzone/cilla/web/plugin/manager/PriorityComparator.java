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
package org.shredzone.cilla.web.plugin.manager;

import java.lang.reflect.Method;
import java.util.Comparator;

import org.shredzone.cilla.web.plugin.annotation.Priority;

/**
 * Compares two objects by their {@link Priority} annotations. For nonexisting
 * {@link Priority} annotations, a priority of 0 is assumed.
 *
 * @author Richard "Shred" Körber
 */
public class PriorityComparator<T> implements Comparator<T> {

    private final Class<T> type;

    /**
     * Creates a new {@link PriorityComparator}.
     *
     * @param type
     *            Type of this {@link PriorityComparator}.
     */
    public PriorityComparator(Class<T> type) {
        this.type = type;
    }

    @Override
    public int compare(T o1, T o2) {
        return Comparator.comparingInt(this::getPriority).reversed().compare(o1, o2);
    }

    /**
     * Gets the priority of an object. It first checks for {@link Priority} annotations at
     * the type's methods, then for a {@link Priority} annotation at the class.
     *
     * @param obj
     *            Object to check
     * @return priority
     */
    private int getPriority(T obj) {
        Class<?> clazz = obj.getClass();

        if (type != null) {
            for (Method m : type.getMethods()) {
                try {
                    Method impl = clazz.getMethod(m.getName(), m.getParameterTypes());
                    Priority p = impl.getAnnotation(Priority.class);
                    if (p != null) {
                        return p.value();
                    }
                } catch (NoSuchMethodException ex) {
                    // Should never happen because obj is an implementation of T and
                    // thus must have all methods of T.
                    throw new InternalError(ex);
                }
            }
        }

        Priority p = clazz.getAnnotation(Priority.class);
        if (p != null) {
            return p.value();
        }

        return 0;
    }

}
