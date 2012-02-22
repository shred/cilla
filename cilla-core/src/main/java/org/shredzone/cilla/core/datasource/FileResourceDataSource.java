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
package org.shredzone.cilla.core.datasource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.shredzone.cilla.core.model.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ResourceDataSource} implementation that stores the resource in a file system.
 *
 * @author Richard "Shred" Körber
 */
public class FileResourceDataSource implements ResourceDataSource {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Store store;
    private final File file;

    /**
     * Creates a new {@link FileResourceDataSource}.
     *
     * @param store
     *            related {@link Store} entry, must be persisted
     * @param basedir
     *            base directory of the store in the file system
     */
    public FileResourceDataSource(Store store, File basedir) {
        if (store.getId() == 0) {
            throw new IllegalArgumentException("store is not persisted");
        }

        this.store = store;

        File storeDir = new File(basedir, String.valueOf(store.getId() % 10));
        this.file = new File(storeDir, String.valueOf(store.getId()));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        if (!(file.exists() && file.isFile())) {
            throw new IOException("Missing resource for store ID " + store.getId());
        }
        return new FileInputStream(file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    @Override
    public String getContentType() {
        return store.getContentType();
    }

    @Override
    public String getName() {
        return store.getName();
    }

    @Override
    public long getId() {
        return store.getId();
    }

    @Override
    public Long getLength() {
        return file.length();
    }

    @Override
    public Date getLastModified() {
        return store.getLastModified();
    }

    @Override
    public void delete() {
        if (!file.delete()) {
            log.error("Could not delete resource file for store ID {}", store.getId());
        }
    }

}
