/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2017 Richard "Shred" Körber
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
package org.shredzone.cilla.web.image;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.web.plugin.ImageProcessor;
import org.shredzone.commons.view.exception.ViewException;

/**
 * A service for processing an image resource. External {@link ImageProcessor} plugins
 * are requested to generate an image of the given type. The result is cached.
 *
 * @author Richard "Shred" Körber
 */
public interface ImageProcessorService {

    /**
     * Processes a stored image.
     *
     * @param store
     *            {@link ResourceDataSource} containing the image to be rendered
     * @param origin
     *            {@link ImageOrigin} of the stored image
     * @param type
     *            Requested processed image type. Depends on the blog implementation.
     * @return {@link ImageProcessorResult} with the processed image, or {@code null} if
     *         no {@link ImageProcessor} was able to provide this image.
     */
    ImageProcessorResult process(ResourceDataSource image, ImageOrigin origin, String type)
                throws ViewException;

}
