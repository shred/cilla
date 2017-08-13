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
package org.shredzone.cilla.site.image;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.web.image.ImageOrigin;
import org.shredzone.cilla.web.image.ImageProcessorResult;
import org.shredzone.cilla.web.plugin.ImageProcessor;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;

import net.coobird.thumbnailator.Thumbnails;

/**
 * A simple {@link ImageProcessor} that returns a scaled image. The requested type must
 * be the scaled dimension of the image (e.g. "200px").
 * <p>
 * Note that you should limit the available image dimensions. Otherwise an attacker could
 * request all kind of random dimensions and fill up the image cache.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ScalingImageProcessor implements ImageProcessor {

    private final Pattern SIZE_PATTERN = Pattern.compile("(\\d+)px");

    @Override
    public ImageProcessorResult process(ResourceDataSource image, ImageOrigin source, String type)
                throws ViewException, IOException {
        if (type == null) {
            return null;
        }

        Matcher m = SIZE_PATTERN.matcher(type);
        if (!m.matches()) {
            return null;
        }
        int maxSize = Integer.parseInt(m.group(1));

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            String contentType;
            Thumbnails.Builder<? extends InputStream> builder = Thumbnails.of(image.getInputStream());

            builder = builder.size(maxSize, maxSize);

            if (maxSize >= 256) {
                builder = builder.outputFormat("jpeg");
                contentType = "image/jpeg";
            } else {
                builder = builder.outputFormat("png");
                contentType = "image/png";
            }

            builder.toOutputStream(out);

            return new ImageProcessorResult(out.toByteArray(), contentType);
        }
    }

}
