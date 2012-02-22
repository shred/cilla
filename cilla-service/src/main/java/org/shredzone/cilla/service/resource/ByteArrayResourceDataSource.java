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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.shredzone.cilla.core.datasource.ResourceDataSource;

/**
 * A {@link ResourceDataSource} that delegates to another {@link ResourceDataSource}, but
 * keeps separate data in a byte array. This {@link ResourceDataSource} is used to return
 * scaled images of a full-scale image.
 *
 * @author Richard "Shred" Körber
 */
public class ByteArrayResourceDataSource implements ResourceDataSource {

    private final ResourceDataSource origin;
    private final String contentType;
    private final byte[] data;

    /**
     * Creates a new {@link ByteArrayResourceDataSource}.
     *
     * @param origin
     *            {@link ResourceDataSource} that is referenced by this instance
     * @param contentType
     *            Content type of the data
     * @param data
     *            data
     */
    public ByteArrayResourceDataSource(ResourceDataSource origin, String contentType, byte[] data) {
        this.origin = origin;
        this.contentType = contentType;
        this.data = data;
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation streams the byte array instead.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(data);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation throws an exception.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new UnsupportedOperationException("datasource is read only");
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns the byte array's content type.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getName() {
        return origin.getName();
    }

    @Override
    public long getId() {
        return origin.getId();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation returns the length of the byte array.
     */
    @Override
    public Long getLength() {
        return Long.valueOf(data.length);
    }

    @Override
    public Date getLastModified() {
        return origin.getLastModified();
    }

    /**
     * {@inheritDoc}
     * <p>
     * This implementation throws an exception.
     */
    @Override
    public void delete() {
        throw new UnsupportedOperationException("datasource is read only");
    }

}
