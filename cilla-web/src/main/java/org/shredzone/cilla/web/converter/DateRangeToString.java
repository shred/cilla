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
package org.shredzone.cilla.web.converter;

import org.shredzone.cilla.service.search.DateRange;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a {@link DateRange} to a String.
 *
 * @author Richard "Shred" Körber
 */
public class DateRangeToString implements Converter<DateRange, String> {

    @Override
    public String convert(DateRange source) {
        return (source != null ? source.toString() : null);
    }

}
