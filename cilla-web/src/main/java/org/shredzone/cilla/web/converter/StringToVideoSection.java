/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2018 Richard "Shred" Körber
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

import org.shredzone.cilla.core.model.EmbedSection;
import org.shredzone.cilla.core.model.VideoSection;
import org.shredzone.cilla.core.repository.SectionDao;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * Converts a String to a {@link EmbedSection}.
 *
 * @author Richard "Shred" Körber
 */
public class StringToVideoSection implements Converter<String, VideoSection> {

    private @Resource SectionDao sectionDao;

    @Override
    public VideoSection convert(String string) {
        try {
            return (VideoSection) sectionDao.fetch(Long.parseLong(string));
        } catch (NumberFormatException | ClassCastException ex) {
            LoggerFactory.getLogger(getClass()).debug("no valid video section id '{}'", string, ex);
            return null;
        }
    }

}
