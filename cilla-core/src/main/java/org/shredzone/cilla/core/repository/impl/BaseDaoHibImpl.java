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

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.shredzone.cilla.core.model.BaseModel;
import org.shredzone.cilla.core.repository.BaseDao;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base implementation of {@link BaseDao}.
 *
 * @param <T>
 *            Type of entity
 * @author Richard "Shred" Körber
 */
@Transactional
public abstract class BaseDaoHibImpl<T extends BaseModel> implements BaseDao<T> {

    private @Resource SessionFactory sessionFactory;

    /**
     * Gets the current {@link Session}.
     *
     * @return Current {@link Session}
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Returns the type of this entity.
     *
     * @return Entity type
     */
    protected abstract Class<T> getType();

    @Override
    public T fetch(long id) {
        return getCurrentSession().get(getType(), id);
    }

    @Override
    public void persist(T data) {
        getCurrentSession().persist(data);
    }

    @SuppressWarnings("unchecked")
    @Override
    public T merge(T data) {
        return (T) getCurrentSession().merge(data);
    }

    @Override
    public void delete(T data) {
        getCurrentSession().delete(data);
    }

}
