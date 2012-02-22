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

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.shredzone.cilla.core.model.Authority;
import org.shredzone.cilla.core.repository.AuthorityDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link AuthorityDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("authorityDao")
@Transactional
public class AuthorityDaoHibImpl extends BaseDaoHibImpl<Authority> implements AuthorityDao {

    @Transactional(readOnly = true)
    @Override
    public Authority fetch(long id) {
        return (Authority) getCurrentSession().get(Authority.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Authority");
        return ((Number) q.uniqueResult()).longValue();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Authority> fetchAll() {
        return getCurrentSession().getNamedQuery("authority.all").list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Authority.class);
    }

    @Override
    public Authority fetchByName(String name) {
        return (Authority) getCurrentSession()
                .getNamedQuery("authority.byName")
                .setParameter("name", name)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Authority> fetchByNames(Collection<String> names) {
        return getCurrentSession().createCriteria(Authority.class)
                .add(Restrictions.in("name", names))
                .list();
    }

}
