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
package org.shredzone.cilla.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import org.primefaces.model.UploadedFile;

/**
 * A {@link DataSource} that wraps a PrimeFaces {@link UploadedFile}.
 *
 * @author Richard "Shred" Körber
 */
public class UploadedFileDataSource implements DataSource {

    private final UploadedFile upload;

    /**
     * Creates a new {@link UploadedFileDataSource} wrapping the given
     * {@link UploadedFile}.
     *
     * @param upload
     *            {@link UploadedFile} to wrap
     */
    public UploadedFileDataSource(UploadedFile upload) {
        this.upload = upload;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return upload.getInputstream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        throw new IOException("DataSource is read only");
    }

    @Override
    public String getContentType() {
        return upload.getContentType();
    }

    @Override
    public String getName() {
        return upload.getFileName();
    }

    /**
     * Creates a {@link DataHandler} for this {@link DataSource}.
     *
     * @return {@link DataHandler}
     */
    public DataHandler toDataHandler() {
        return new DataHandler(this);
    }

}
