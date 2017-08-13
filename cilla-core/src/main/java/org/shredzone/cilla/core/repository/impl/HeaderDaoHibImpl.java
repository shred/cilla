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
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.repository.HeaderDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link HeaderDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("headerDao")
@Transactional
public class HeaderDaoHibImpl extends BaseDaoHibImpl<Header> implements HeaderDao {

    private final Random rnd = new Random();  // no secure random generator required

    @Override
    protected Class<Header> getType() {
        return Header.class;
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Header", Number.class);
        return q.uniqueResult().longValue();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Header.class);
    }

    @Override
    public List<Header> fetchAll() {
        return getCurrentSession()
                .createQuery("FROM Header ORDER BY creation DESC", Header.class)
                .list();
    }

    @Override
    public List<Header> fetchEnabled() {
        return getCurrentSession()
                .createQuery("FROM Header WHERE enabled=:enabled ORDER BY creation DESC", Header.class)
                .setParameter("enabled", Boolean.TRUE)
                .list();
    }

    @Override
    public Header fetchByName(String name) {
        return (Header) getCurrentSession()
                .createQuery("FROM Header WHERE name=:name")
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public Header fetchRandomHeader() {
        Criterion restriction = Restrictions.eq("enabled", Boolean.TRUE);

        Number count = (Number) criteria()
                .add(restriction)
                .setProjection(Projections.rowCount())
                .uniqueResult();

        Header result = null;

        if (count.intValue() != 0) {
            int index = rnd.nextInt(count.intValue());
            result = (Header) criteria()
                    .add(restriction)
                    .setFirstResult(index)
                    .setMaxResults(1)
                    .uniqueResult();
        }

        return result;
    }

}
