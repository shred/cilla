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
package org.shredzone.cilla.service.resource;

import java.io.IOException;

import javax.activation.DataSource;

import org.shredzone.cilla.core.model.Store;
import org.shredzone.cilla.ws.ImageProcessing;

/**
 * Image processor engine, for scaling and other process operations on images. The
 * processed image is long-term cached, so the processor is very fast for images that have
 * already been processed before.
 *
 * @author Richard "Shred" Körber
 */
public interface ImageProcessor {

    /**
     * Processes an image.
     *
     * @param ds
     *            {@link DataSource} of the original image
     * @param id
     *            {@link Store} id that is used as cache key
     * @param process
     *            {@link ImageProcessing} with processing information, must not be
     *            {@code null}
     * @return Processed image as {@link ImageProcessorResult}
     */
    ImageProcessorResult process(DataSource ds, long id, ImageProcessing process) throws IOException;

}
