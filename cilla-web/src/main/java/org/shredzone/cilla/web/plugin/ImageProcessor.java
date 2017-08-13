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
package org.shredzone.cilla.web.plugin;

import java.io.IOException;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.web.image.ImageOrigin;
import org.shredzone.cilla.web.image.ImageProcessorResult;
import org.shredzone.commons.view.exception.ViewException;

/**
 * An image processor plug-in. It checks if it can provide the requested image type,
 * and if so, returns the processed image.
 *
 * @author Richard "Shred" Körber
 */
public interface ImageProcessor {

    /**
     * Processes the image if the requested type can be provided.
     *
     * @param image
     *            {@link ResourceDataSource} containing the original image
     * @param source
     *            {@link ImageOrigin} describing the image source
     * @param type
     *            Requested image type, can be {@code null} if a default (unprocessed)
     *            image is requested.
     * @return {@link ImageProcessorResult} containing the processed image, or
     *         {@code null} if this image processor was unable to provide the image.
     * @throws ViewException
     *             If the image processor should be able to provide the image, but failed
     *             to process it.
     * @throws IOException
     *             If the image processor could not read the image. For your convenience.
     */
    ImageProcessorResult process(ResourceDataSource image, ImageOrigin source, String type)
                throws ViewException, IOException;

}
