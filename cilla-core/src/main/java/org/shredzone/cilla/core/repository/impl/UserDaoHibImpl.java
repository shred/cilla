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
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.UserDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UserDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("userDao")
@Transactional
public class UserDaoHibImpl extends BaseDaoHibImpl<User> implements UserDao {

    @Transactional(readOnly = true)
    @Override
    public User fetch(long id) {
        return getCurrentSession().get(User.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM User", Number.class);
        return q.uniqueResult().longValue();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(User.class);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<User> fetchAll() {
        return getCurrentSession()
                .createQuery("FROM User u ORDER BY u.login")
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public User fetchByLogin(String login) {
        return (User) getCurrentSession()
                .createQuery("FROM User WHERE login=:login")
                .setParameter("login", login)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<User> fetchAllWithRole(Role role) {
        return getCurrentSession()
                .createQuery("FROM User WHERE role=:role")
                .setParameter("role", role)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<User> fetchAllWithAuthority(String authority) {
        return getCurrentSession()
                .createQuery("SELECT u FROM User u, IN (u.role.authorities) a" +
                       " WHERE a.name = :authority")
                .setParameter("authority", authority)
                .list();
    }

}
