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
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.repository.SectionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link SectionDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("sectionDao")
@Transactional
public class SectionDaoHibImpl extends BaseDaoHibImpl<Section> implements SectionDao {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    protected Class<Section> getType() {
        return Section.class;
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Section", Number.class);
        return q.uniqueResult().longValue();
    }

    @Override
    public List<Section> fetchAll() {
        return getCurrentSession()
                .createQuery("FROM Section", Section.class)
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Section.class);
    }

    @Override
    public Criteria criteria(Class<? extends Section> type) {
        return getCurrentSession().createCriteria(type);
    }

    @Override
    public <T extends Section> T fetchAs(long id, Class<T> clazz) {
        Section result = fetch(id);
        try {
            return clazz.cast(result);
        } catch (ClassCastException ex) {
            log.warn("Section id {} type mismatch", id, ex);
            return null;
        }
    }

}
