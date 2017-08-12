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
package org.shredzone.cilla.core.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.shredzone.cilla.core.datasource.FileResourceDataSource;
import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Store;
import org.shredzone.cilla.core.repository.StoreDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link StoreDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("storeDao")
@Transactional
public class StoreDaoHibImpl extends BaseDaoHibImpl<Store> implements StoreDao {

    private @Value("${resource.path}") String basePath;

    @Transactional(readOnly = true)
    @Override
    public Store fetch(long id) {
        return getCurrentSession().get(Store.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Store", Number.class);
        return q.uniqueResult().longValue();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Store> fetchAll() {
        return getCurrentSession()
                .createQuery("FROM Store")
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Store.class);
    }

    @Override
    public ResourceDataSource access(Store resource) throws IOException {
        if (resource.getId() == 0) {
            throw new IllegalArgumentException("store is not persisted");
        }

        return new FileResourceDataSource(resource, new File(basePath));
    }

}
