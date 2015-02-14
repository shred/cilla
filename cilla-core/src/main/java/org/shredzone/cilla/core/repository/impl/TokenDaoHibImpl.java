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
import org.shredzone.cilla.core.model.Token;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.TokenDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link TokenDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("tokenDao")
@Transactional
public class TokenDaoHibImpl extends BaseDaoHibImpl<Token> implements TokenDao {

    @Transactional(readOnly = true)
    @Override
    public Token fetch(long id) {
        return (Token) getCurrentSession().get(Token.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Token");
        return ((Number) q.uniqueResult()).longValue();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Token> fetchAll() {
        return getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Token")
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Token.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Token fetch(User user, String site) {
        return (Token) getCurrentSession()
                .createQuery("FROM Token WHERE user=:user AND site=:site")
                .setParameter("user", user)
                .setParameter("site", site)
                .uniqueResult();
    }

    @Override
    public void store(User user, String site, String token, String secret) {
        Token tok = fetch(user, site);
        if (tok != null) {
            tok.setToken(token);
            tok.setSecret(secret);
        } else {
            tok = new Token();
            tok.setUser(user);
            tok.setSite(site);
            tok.setToken(token);
            tok.setSecret(secret);
            persist(tok);
        }
    }

    @Override
    public void remove(User user, String site) {
        Token tok = fetch(user, site);
        if (tok != null) {
            delete(tok);
        }
    }

}
