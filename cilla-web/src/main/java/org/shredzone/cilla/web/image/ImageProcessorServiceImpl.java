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
import org.shredzone.cilla.web.plugin.ImageProcessor;
import org.shredzone.cilla.web.plugin.manager.ImageProcessorManager;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * {@link ImageProcessorService} implementation.
 *
 * @author Richard "Shred" Körber
 */
@Service
public class ImageProcessorServiceImpl implements ImageProcessorService {

    private @Resource ImageProcessorManager imageProcessorManager;

    @Override
    @Cacheable(value = "processedImages", keyGenerator = "imageCacheKeyGenerator")
    public ImageProcessorResult process(ResourceDataSource image, ImageOrigin origin, String type)
                throws ViewException {
        try {
            for (ImageProcessor processor : imageProcessorManager.getImageProcessors()) {
                ImageProcessorResult result = processor.process(image, origin, type);
                if (result != null) {
                    return result;
                }
            }
            return null;
        } catch (IOException ex) {
            throw new ViewException("Could not process image, " + origin.name() + " "
                + image.getId() + ", type=" + type);
        }
    }

}
