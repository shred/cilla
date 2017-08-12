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
package org.shredzone.cilla.admin;

import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.LongConverter;

import org.shredzone.cilla.ws.category.CategoryDto;
import org.shredzone.cilla.ws.category.CategoryWs;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.stereotype.Component;

/**
 * Converts a {@link CategoryDto} to a {@link String}, and vice versa. The string contains
 * the ID of the referenced category.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class CategoryConverter extends LongConverter {

    @Resource
    private CategoryWs categoryWs;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            Long id = (Long) super.getAsObject(context, component, value);
            return categoryWs.fetch(id);
        } catch (CillaServiceException ex) {
            throw new IllegalArgumentException("could not fetch category with id " + value, ex);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null || !(value instanceof CategoryDto)) {
            throw new ConverterException("Not a CategoryDto object");
        }

        CategoryDto cat = (CategoryDto) value;
        return super.getAsString(context, component, cat.getId());
    }

}
