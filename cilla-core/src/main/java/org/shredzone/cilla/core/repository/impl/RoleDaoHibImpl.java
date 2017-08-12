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
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.repository.RoleDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link RoleDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("roleDao")
@Transactional
public class RoleDaoHibImpl extends BaseDaoHibImpl<Role> implements RoleDao {

    @Transactional(readOnly = true)
    @Override
    public Role fetch(long id) {
        return getCurrentSession().get(Role.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Role", Number.class);
        return q.uniqueResult().longValue();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Role.class);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Role> fetchAll() {
        return getCurrentSession().createQuery("FROM Role ORDER BY name").list();
    }

    @Override
    public Role fetchByName(String name) {
        return (Role) getCurrentSession().createQuery("FROM Role WHERE name = :name")
                .setParameter("name", name)
                .uniqueResult();
    }

}
