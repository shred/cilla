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

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.repository.SectionDao;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String to a {@link GallerySection}.
 *
 * @author Richard "Shred" Körber
 */
public class StringToGallerySection implements Converter<String, GallerySection> {

    private @Resource SectionDao sectionDao;

    @Override
    public GallerySection convert(String string) {
        try {
            return (GallerySection) sectionDao.fetch(Long.parseLong(string));
        } catch (NumberFormatException ex) {
            return null;
        } catch (ClassCastException ex) {
            return null;
        }
    }

}
