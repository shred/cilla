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

import java.io.IOException;

import javax.annotation.Resource;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Store;
import org.shredzone.cilla.core.repository.StoreDao;
import org.shredzone.commons.view.exception.PageNotFoundException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;

/**
 * Provides a processed image. The processed image is cached. The cache is used until
 * the original image {@link Store#getVersion()} changes.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ImageProvider {

    private @Resource ImageProcessorService imageProcessorService;
    private @Resource StoreDao storeDao;

    /**
     * Provide a processed image.
     *
     * @param image
     *            {@link Store} containing the unprocessed image
     * @param source
     *            {@link ImageOrigin} describing the image origin
     * @param type
     *            Requested image type, or {@code null} for a default (unprocessed) image.
     *            A default type can still be a processed image, e.g. a watermark can be
     *            applied.
     * @return {@link ResourceDataSource} containing the processed image, or {@code null}
     *         if the requested type could not be provided.
     */
    public ResourceDataSource provide(Store image, ImageOrigin source, String type)
            throws ViewException {
        try {
            ResourceDataSource origin = storeDao.access(image);

            ImageProcessorResult pr = imageProcessorService.process(origin, source, type);
            if (pr != null) {
                return new ByteArrayResourceDataSource(origin, source, type, pr.getContentType(), pr.getData());
            }

            if (type != null) {
                throw new PageNotFoundException("Could not find image " + image.getId());
            }

            return origin;
        } catch (IOException ex) {
            throw new ViewException("Failed to provide image " + image.getId(), ex);
        }
    }

}
