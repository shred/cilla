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
package org.shredzone.cilla.web.plugin;

import org.shredzone.cilla.web.plugin.annotation.Priority;
import org.shredzone.cilla.ws.ImageProcessing;

/**
 * A factory that parses the image type name and creates a matching
 * {@link ImageProcessing} objects. It is used for providing different image types (e.g.
 * "thumb", "200px") that can be used in external links.
 * <p>
 * The implementation can be annotated with {@link Priority} in order to change the
 * priority of the matchers.
 *
 * @author Richard "Shred" Körber
 */
public interface ImageProcessingFactory {

    /**
     * Creates an {@link ImageProcessing} object by a type name.
     *
     * @param type
     *            Type name
     * @return {@link ImageProcessing} that was created, or {@code null} if this plugin
     *         is unable to provide a matching {@link ImageProcessing}.
     */
    ImageProcessing createImageProcessing(String type);

}
