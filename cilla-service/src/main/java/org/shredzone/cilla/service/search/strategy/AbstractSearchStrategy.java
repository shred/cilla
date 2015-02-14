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
package org.shredzone.cilla.service.search.strategy;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.util.DateUtils;
import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.service.search.impl.SearchResultImpl;
import org.shredzone.cilla.ws.PageOrder;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.beans.factory.annotation.Value;

/**
 * Abstract implementation of {@link SearchStrategy}, containing commonly used methods.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractSearchStrategy implements SearchStrategy {
    private @Value("${page.order}") PageOrder pageOrder;

    private @Resource PageDao pageDao;
    private @Resource SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public Set<Integer> fetchPageDays(SearchResultImpl result, Calendar calendar) throws CillaServiceException {
        if (calendar == null) {
            return Collections.emptySet();
        }

        Calendar start = DateUtils.beginningOfMonth(calendar);
        Calendar end = DateUtils.beginningOfNextMonth(calendar);

        Criteria crit = createCriteria(result);
        crit.add(Restrictions.ge(pageOrder.getColumn(), start.getTime()));
        crit.add(Restrictions.lt(pageOrder.getColumn(), end.getTime()));

        // Sadly there is no HQL function to get the calendary day of a timestamp,
        // so we have to do all the hard work ourselves, to avoid SQL dialects. :(
        crit.setProjection(Projections.property(pageOrder.getColumn()));

        Calendar cal = Calendar.getInstance(calendar.getTimeZone());

        Set<Integer> daysResult = new HashSet<>();
        for (Date day : (List<Date>) crit.list()) {
            cal.setTime(day);
            daysResult.add(cal.get(Calendar.DAY_OF_MONTH));
        }

        return daysResult;
    }

    /**
     * Gets the current {@link Session}.
     */
    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Creates a {@link Criteria} object for the filter given in the
     * {@link SearchResultImpl}.
     */
    protected Criteria createCriteria(SearchResultImpl result) throws CillaServiceException {
        Criteria crit = pageDao.criteria();
        crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        crit.add(Restrictions.and(
                    Restrictions.isNotNull("publication"),
                    Restrictions.le("publication", result.getNow())
        ));

        crit.add(Restrictions.or(
                    Restrictions.isNull("expiration"),
                    Restrictions.gt("expiration", result.getNow())
        ));

        crit.add(Restrictions.eq("hidden", false));

        FilterModel filter = result.getFilter();
        if (filter != null) {
            if (filter.getCategory() != null) {
                // I'd prefer to use Restrictions.in instead, but this seems to be difficult.
                // http://stackoverflow.com/questions/407737/hibernate-criteria-querying-tables-in-nm-relationship
                Disjunction dis = Restrictions.disjunction();
                result.getEffectiveCategories().stream()
                        .mapToLong(Category::getId)
                        .forEach(id -> dis.add(Restrictions.idEq(id)));
                crit.createCriteria("categories").add(dis);
            }

            if (filter.getTag() != null) {
                long tagId = filter.getTag().getId();
                Disjunction dis = Restrictions.disjunction();

                // All pages with the requested tag
                crit.createAlias("tags", "tt");
                dis.add(Restrictions.eq("tt.id", tagId));

                // All pages with pictures in a gallery section having the requested tag
                DetachedCriteria subcrit = DetachedCriteria.forClass(GallerySection.class);
                subcrit.createCriteria("pictures").createCriteria("tags").add(Restrictions.idEq(tagId));
                subcrit.setProjection(Projections.distinct(Projections.property("page.id")));
                dis.add(Subqueries.propertyIn("id", subcrit));

                crit.add(dis);
            }

            if (filter.getCreator() != null) {
                crit.add(Restrictions.eq("creator", filter.getCreator()));
            }

            if (filter.getPage() != null) {
                crit.add(Restrictions.idEq(filter.getPage().getId()));
            }

            if (filter.getDate() != null) {
                DateRange dr = filter.getDate();
                PageOrder order = (filter.getOrder() != null ? filter.getOrder() : PageOrder.PUBLICATION);
                crit.add(Restrictions.between(order.getColumn(), dr.getFromDate().getTime(), dr.getThruDate().getTime()));
            }

            if (filter.getQuery() != null) {
                // No challenge protected pages for context search, because protected
                // contents may be revealed in the search results.
                crit.add(Restrictions.isNull("challenge"));
            }
        }

        return crit;
    }

    /**
     * Creates a {@link Criteria} that also takes the {@link PaginatorModel} into account.
     */
    protected Criteria createPaginatedCriteria(SearchResultImpl result) throws CillaServiceException {
        Criteria crit = createCriteria(result);

        crit.addOrder(Order.desc("sticky"));

        FilterModel filter = result.getFilter();
        if (filter != null && filter.getOrder() != null) {
            crit.addOrder(Order.desc(filter.getOrder().getColumn()));
        } else {
            crit.addOrder(Order.desc(pageOrder.getColumn()));
        }

        if (result.getPaginator() != null) {
            crit.setFirstResult(result.getPaginator().getFirst());
            crit.setMaxResults(result.getPaginator().getPerPage());
        }

        return crit;
    }

}
