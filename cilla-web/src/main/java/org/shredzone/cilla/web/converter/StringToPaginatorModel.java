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

import org.shredzone.cilla.service.search.PaginatorModel;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String to a {@link PaginatorModel}.
 *
 * @author Richard "Shred" Körber
 */
public class StringToPaginatorModel implements Converter<String, PaginatorModel> {

    @Override
    public PaginatorModel convert(String string) {
        PaginatorModel paginator = new PaginatorModel();
        try {
            int page = Integer.parseInt(string);
            if (page >= 0 && page < 100000) {
                paginator.setSelectedPage(page);
            }
        } catch (NumberFormatException ex) {
            // Use page 0 for unparseable page numbers
        }
        return paginator;
    }

}
