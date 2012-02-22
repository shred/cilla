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
package org.shredzone.cilla.core.repository;

import java.io.IOException;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Store;

/**
 * DAO for {@link Store} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface StoreDao extends BaseDao<Store> {

    /**
     * Gives access to the store's backing resource.
     *
     * @param store
     *            persisted {@link Store} to get the resource of
     * @return {@link ResourceDataSource} of the store's backing resource
     * @throws IOException
     *             if the resource could not be opened
     */
    ResourceDataSource access(Store store) throws IOException;

}
