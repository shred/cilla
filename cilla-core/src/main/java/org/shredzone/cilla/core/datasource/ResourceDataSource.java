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

import java.util.Date;

import javax.activation.DataSource;

import org.apache.lucene.document.Field.Store;

/**
 * An extended {@link DataSource} representing a resource from a {@link Store}.
 *
 * @author Richard "Shred" Körber
 */
public interface ResourceDataSource extends DataSource {

    /**
     * A unique ID for this resource. This is the {@link Store} ID.
     */
    long getId();

    /**
     * Content length of the resource.
     *
     * @return Content length, or {@code null} if not known.
     */
    Long getLength();

    /**
     * Last modification date of the resource.
     *
     * @return Last modification date
     */
    Date getLastModified();

    /**
     * Tries to delete the resource at the store. Note that only the resource at the
     * backing store is deleted, but not the {@link Store} entry itself.
     */
    void delete();

}
