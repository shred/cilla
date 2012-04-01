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

import org.shredzone.cilla.core.event.annotation.EventListener;
import org.shredzone.cilla.core.event.annotation.OnEvent;

/**
 * Offers services for event handling and processing.
 * <p>
 * Event handler classes are annotated with {@link EventListener}. Each event handler
 * method is annotated with {@link OnEvent}.
 *
 * @author Richard "Shred" Körber
 */
public interface EventService {

    /**
     * Fires an event. All {@link EventListener} listening to that event will be notified.
     *
     * @param event
     *            {@link Event} to fire
     */
    void fireEvent(Event<?> event);

}
