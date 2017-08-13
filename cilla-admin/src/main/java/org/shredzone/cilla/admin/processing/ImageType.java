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
package org.shredzone.cilla.admin.processing;

import javax.imageio.ImageIO;

/**
 * Enumeration of available image types.
 *
 * @author Richard "Shred" Körber
 */
public enum ImageType {

    /**
     * PNG image.
     */
    PNG("png", "image/png", null),

    /**
     * JPEG image with high quality.
     */
    JPEG("jpeg", "image/jpeg", null),

    /**
     * JPEG image with low quality (factor 0.85).
     */
    JPEG_LOW("jpeg", "image/jpeg", 0.85f);

    private final String formatName;
    private final String contentType;
    private final Float compression;

    private ImageType(String name, String type, Float compression) {
        this.formatName = name;
        this.contentType = type;
        this.compression = compression;
    }

    /**
     * Content type of the {@link ImageType}.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Format name of the {@link ImageType}. Can be used with {@link ImageIO} or as file
     * suffix.
     */
    public String getFormatName() {
        return formatName;
    }

    /**
     * Compression quality factor, or {@code null} for default/no compression.
     */
    public Float getCompression() {
        return compression;
    }

}
