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

/**
 * Contains a single event.
 *
 * @author Richard "Shred" Körber
 */
public class Event<T> {

    private final EventType type;
    private final T source;

    /**
     * Instantiates a new event.
     *
     * @param type
     *            {@link EventType} of this event
     * @param source
     *            source that triggered the event
     */
    public Event(EventType type, T source) {
        this.type = type;
        this.source = source;
    }

    /**
     * Gets the event type.
     *
     * @return the type
     */
    public EventType getType() {
        return type;
    }

    /**
     * Gets the source that triggered the event.
     *
     * @return the source, may be {@code null}
     */
    public T getSource() {
        return source;
    }

}
