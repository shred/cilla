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

import java.io.Serializable;

/**
 * A serializable result of the {@link ImageProcessor}.
 *
 * @author Richard "Shred" Körber
 */
public class ImageProcessorResult implements Serializable {
    private static final long serialVersionUID = 3434161370077020102L;

    private byte[] data;
    private String contentType;

    /**
     * Processed image, as byte array.
     */
    public byte[] getData()                     { return data; }
    public void setData(byte[] data)            { this.data = data; }

    /**
     * Image's content type.
     */
    public String getContentType()              { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

}
