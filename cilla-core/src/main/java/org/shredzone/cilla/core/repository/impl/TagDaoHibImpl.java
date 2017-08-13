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

import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.repository.TagDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link TagDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("tagDao")
@Transactional
public class TagDaoHibImpl extends BaseDaoHibImpl<Tag> implements TagDao {

    @Override
    protected Class<Tag> getType() {
        return Tag.class;
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Tag", Number.class);
        return q.uniqueResult().longValue();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Tag.class);
    }

    @Override
    public List<Tag> fetchAll() {
        return getCurrentSession().createQuery("FROM Tag ORDER BY name", Tag.class)
                .list();
    }

    @Override
    public Tag fetchByName(String name) {
        return getCurrentSession()
                .createQuery("FROM Tag WHERE name=:name", Tag.class)
                .setParameter("name", name)
                .uniqueResult();
    }

    @Override
    public Tag fetchOrCreate(String name) {
        Tag tag = fetchByName(name);
        if (tag == null) {
            tag = new Tag();
            tag.setName(name);
            persist(tag);
        }
        return tag;
    }

    @Override
    public SortedSet<Tag> fetchAllUsedTags(boolean pagesOnly, boolean publicOnly) {
        Date now = new Date();
        SortedSet<Tag> result = new TreeSet<>();

        if (publicOnly) {
            result.addAll(getCurrentSession()
                    .createQuery("SELECT DISTINCT t"
                        + " FROM Page p JOIN p.tags t"
                        + " WHERE (p.publication IS NOT NULL AND p.publication<=:now)"
                        + " AND (p.expiration IS NULL OR p.expiration>:now)"
                        + " AND p.challenge IS NULL AND p.responsePattern IS NULL", Tag.class)
                    .setParameter("now", now)
                    .getResultList());
        } else {
            result.addAll(getCurrentSession()
                    .createQuery("SELECT DISTINCT t"
                        + " FROM Page p JOIN p.tags t", Tag.class)
                    .getResultList());
        }

        if (!pagesOnly) {
            if (publicOnly) {
                result.addAll(getCurrentSession()
                        .createQuery("SELECT DISTINCT t"
                            + " FROM Picture pi JOIN pi.tags t JOIN pi.gallery.page p"
                            + " WHERE (p.publication IS NOT NULL AND p.publication<=:now)"
                            + " AND (p.expiration IS NULL OR p.expiration>:now)"
                            + " AND p.challenge IS NULL AND p.responsePattern IS NULL", Tag.class)
                        .setParameter("now", now)
                        .getResultList());
            } else {
                result.addAll(getCurrentSession()
                        .createQuery("SELECT DISTINCT t"
                            + " FROM Picture p JOIN p.tags t", Tag.class)
                        .getResultList());
            }
        }

        return result;
    }

}
