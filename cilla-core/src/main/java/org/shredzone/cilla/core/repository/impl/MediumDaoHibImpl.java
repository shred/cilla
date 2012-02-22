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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.MediumDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link MediumDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("mediumDao")
@Transactional
public class MediumDaoHibImpl extends BaseDaoHibImpl<Medium> implements MediumDao {

    @Transactional(readOnly = true)
    @Override
    public Medium fetch(long id) {
        return (Medium) getCurrentSession().get(Medium.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Medium");
        return ((Number) q.uniqueResult()).longValue();
    }

    @Transactional(readOnly = true)
    @Override
    @SuppressWarnings("unchecked")
    public List<Medium> fetchAll() {
        return getCurrentSession()
                .createQuery("FROM Medium ORDER BY image.name")
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Medium.class);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll(Page page) {
        Query q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Medium WHERE page=:page")
                .setParameter("page", page);
        return ((Number) q.uniqueResult()).longValue();
    }

    @Transactional(readOnly = true)
    @Override
    @SuppressWarnings("unchecked")
    public List<Medium> fetchAll(Page page) {
        return getCurrentSession()
                .createQuery("FROM Medium WHERE page=:page ORDER BY image.name")
                .setParameter("page", page)
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Medium fetchByName(Page page, String name) {
        Query q = getCurrentSession()
                .createQuery("FROM Medium WHERE page=:page AND image.name=:name")
                .setParameter("page", page)
                .setParameter("name", name);
        return (Medium) q.uniqueResult();
    }

}
