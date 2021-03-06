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
package org.shredzone.cilla.web.renderer;

import java.io.IOException;

/**
 * A strategy for rendering a calendar. The strategy should write a HTML calendar by
 * using the data provided by the {@link CalendarGenerator}.
 * <p>
 * The implementation may be prototype scoped.
 *
 * @author Richard "Shred" Körber
 */
public interface CalendarRenderStrategy {

    /**
     * Writes a calendar.
     *
     * @param generator
     *            {@link CalendarGenerator} providing all calendar data
     * @param out
     *            output to write the calendar HTML to
     */
    void write(CalendarGenerator generator, Appendable out) throws IOException;

}
