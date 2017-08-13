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
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Language;
import org.shredzone.cilla.core.repository.LanguageDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link LanguageDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("languageDao")
@Transactional
public class LanguageDaoHibImpl extends BaseDaoHibImpl<Language> implements LanguageDao {

    @Override
    public Language fetch(long id) {
        return getCurrentSession().get(Language.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Language", Number.class);
        return q.uniqueResult().longValue();
    }

    @Override
    public List<Language> fetchAll() {
        return getCurrentSession().createQuery("FROM Language ORDER BY locale", Language.class).list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Language.class);
    }

    @Override
    public Language fetchForLocale(Locale locale) {
        return (Language) getCurrentSession()
                .createQuery("FROM Language WHERE locale=:locale")
                .setParameter("locale", locale.toString())
                .uniqueResult();
    }

}
