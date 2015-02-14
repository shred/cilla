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

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.util.DateUtils;
import org.shredzone.cilla.ws.PageOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link PageDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("pageDao")
@Transactional
public class PageDaoHibImpl extends BaseDaoHibImpl<Page> implements PageDao {

    private @Value("${page.order}") PageOrder pageOrder;

    @Transactional(readOnly = true)
    @Override
    public Page fetch(long id) {
        return (Page) getCurrentSession().get(Page.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Page");
        return ((Number) q.uniqueResult()).longValue();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Page.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Page fetchByName(String name) {
        return (Page) getCurrentSession()
                .createQuery("FROM Page WHERE name=:name")
                .setParameter("name", name)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Page> fetchAll() {
        return getCurrentSession().createQuery("FROM Page").list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Page> fetchAllPublic() {
        return getCurrentSession().createQuery("FROM Page" +
                        " WHERE (publication IS NOT NULL AND publication<=:now)" +
                        " AND (expiration IS NULL OR expiration>:now)" +
                        " AND challenge IS NULL AND responsePattern IS NULL" +
                        " ORDER BY sticky DESC, hidden ASC," + pageOrder.getColumn() + " DESC")
                .setParameter("now", new Date())
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Date[] fetchMinMaxModification() {
        Date now = new Date();

        Criteria crit = getCurrentSession().createCriteria(Page.class);

        crit.add(Restrictions.and(
                    Restrictions.isNotNull("publication"),
                    Restrictions.le("publication", now)
        ));

        crit.add(Restrictions.or(
                    Restrictions.isNull("expiration"),
                    Restrictions.gt("expiration", now)
        ));

        crit.add(Restrictions.eq("hidden", false));

        ProjectionList proj = Projections.projectionList();
        proj.add(Projections.min(pageOrder.getColumn()));
        proj.add(Projections.max(pageOrder.getColumn()));
        crit.setProjection(proj);
        Object[] row = (Object[]) crit.uniqueResult();

        Date[] result = new Date[2];
        result[0] = (Date) row[0];
        result[1] = (Date) row[1];
        return result;
    }

    @Override
    public Date[] fetchPrevNextMonth(Calendar calendar) {
        Date[] result = new Date[2];

        // Find first page before that month
        Calendar beginning = DateUtils.beginningOfMonth(calendar);
        result[0] = (Date) getCurrentSession()
                .createQuery("SELECT " + pageOrder.getColumn() + " FROM Page" +
                    " WHERE " + pageOrder.getColumn() + "<:month" +
                    " AND (publication IS NOT NULL AND publication<=:now)" +
                    " AND (expiration IS NULL OR expiration>:now)" +
                    " AND hidden=false" +
                    " ORDER BY " + pageOrder.getColumn() + " DESC")
                .setParameter("month", beginning.getTime())
                .setParameter("now", new Date())
                .setMaxResults(1)
                .uniqueResult();


        // Find first page of that month (or later)
        Calendar ending = DateUtils.beginningOfNextMonth(calendar);
        result[1] = (Date) getCurrentSession()
                .createQuery("SELECT " + pageOrder.getColumn() + " FROM Page" +
                    " WHERE " + pageOrder.getColumn() + ">=:month" +
                    " AND (publication IS NOT NULL AND publication<=:now)" +
                    " AND (expiration IS NULL OR expiration>:now)" +
                    " AND hidden=false" +
                    " ORDER BY " + pageOrder.getColumn() + " ASC")
                .setParameter("month", ending.getTime())
                .setParameter("now", new Date())
                .setMaxResults(1)
                .uniqueResult();

        return result;
    }

    @Override
    public Page fetchPreviousPage(Page page) {
        if (page.isHidden()) {
            // Hidden pages do not have neighbors.
            return null;
        }

        return (Page) getCurrentSession()
                .createQuery("FROM Page" +
                        " WHERE " + pageOrder.getColumn() + "<:current" +
                        " AND (publication IS NOT NULL AND publication<=:now)" +
                        " AND (expiration IS NULL OR expiration>:now)" +
                        " AND hidden=false" +
                        " ORDER BY " + pageOrder.getColumn() + " DESC")
                .setParameter("current", DateUtils.getOrderDate(page, pageOrder))
                .setParameter("now", new Date())
                .setMaxResults(1)
                .uniqueResult();
    }

    @Override
    public Page fetchNextPage(Page page) {
        if (page.isHidden()) {
            // Hidden pages do not have neighbors.
            return null;
        }

        return (Page) getCurrentSession()
                .createQuery("FROM Page" +
                        " WHERE " + pageOrder.getColumn() + ">:current" +
                        " AND (publication IS NOT NULL AND publication<=:now)" +
                        " AND (expiration IS NULL OR expiration>:now)" +
                        " AND hidden=false" +
                        " ORDER BY " + pageOrder.getColumn() + " ASC")
                .setParameter("current", DateUtils.getOrderDate(page, pageOrder))
                .setParameter("now", new Date())
                .setMaxResults(1)
                .uniqueResult();
    }

    @SuppressWarnings({ "unchecked", "cast" })
    @Transactional(readOnly = true)
    @Override
    public List<Page> fetchSameSubject(Page page) {
        if (page.getSubject() == null) {
            return Collections.emptyList();
        }

        return getCurrentSession()
                .createQuery("FROM Page" +
                        " WHERE (subject IS NOT NULL AND subject=:subject)" +
                        " AND (publication IS NOT NULL AND publication<=:now)" +
                        " AND (expiration IS NULL OR expiration>:now)" +
                        " AND hidden=false" +
                        " ORDER BY " + pageOrder.getColumn() + " ASC")
                .setParameter("subject", page.getSubject())
                .setParameter("now", new Date())
                .list();
    }

    @SuppressWarnings({ "unchecked", "cast" })
    @Transactional(readOnly = true)
    @Override
    public List<String> fetchAllSubjects() {
        return getCurrentSession()
                .createQuery("SELECT DISTINCT subject FROM Page" +
                        " ORDER BY subject ASC")
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<String> proposeSubjects(String query, int limit) {
        return getCurrentSession().createCriteria(Page.class)
                .add(Restrictions.like("subject", query, MatchMode.ANYWHERE))
                .setMaxResults(limit)
                .addOrder(Order.asc("subject"))
                .setProjection(Projections.distinct(Projections.property("subject")))
                .list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Page> fetchBadPublishState() {
        return getCurrentSession()
                .createQuery("FROM Page" +
                        " WHERE ((publication IS NOT NULL AND publication<=:now)" +
                        " AND (expiration IS NULL OR expiration>:now)" +
                        " AND publishedState=false) OR" +
                        " (NOT ((publication IS NOT NULL AND publication<=:now)" +
                        " AND (expiration IS NULL OR expiration>:now))" +
                        " AND publishedState=true)")
                .setParameter("now", new Date())
                .list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Page> fetchHavingProperty(String key, String value) {
        if (value != null) {
            return getCurrentSession()
                            .createQuery("FROM Page p" +
                                    " WHERE p.properties[:key]=:value")
                            .setParameter("key", key)
                            .setParameter("value", value)
                            .list();
        } else {
            return getCurrentSession()
                            .createQuery("SELECT p FROM Page p" +
                                    " JOIN p.properties o" +
                                    " WHERE index(o)=:key")
                            .setParameter("key", key)
                            .list();
        }
    }

}
