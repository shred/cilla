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
package org.shredzone.cilla.web.image;

import java.io.Serializable;

import org.shredzone.cilla.web.plugin.ImageProcessor;

/**
 * A serializable result of the {@link ImageProcessor}. The object is immutable.
 *
 * @author Richard "Shred" Körber
 */
public class ImageProcessorResult implements Serializable {
    private static final long serialVersionUID = 3434161370077020102L;

    private final byte[] data;
    private final String contentType;

    /**
     * Create a new {@link ImageProcessorResult}.
     *
     * @param data
     *            Processed image data
     * @param contentType
     *            Content Type
     */
    public ImageProcessorResult(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    /**
     * Processed image, as byte array.
     */
    public byte[] getData()                     { return data; }

    /**
     * Image's content type.
     */
    public String getContentType()              { return contentType; }

}
