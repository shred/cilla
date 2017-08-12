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
package org.shredzone.cilla.ws;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

/**
 * Abstract superclass for all web services. It offers some helper methods.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractWs {

    /**
     * Applies the {@link ListRange} to the given {@link Criteria}.
     *
     * @param lr
     *            {@link ListRange} to apply
     * @param order
     *            Property to order by
     * @param desc
     *            {@code true} for descending order
     * @param crit
     *            {@link Criteria} to apply to
     */
    protected void applyListRange(ListRange lr, String order, boolean desc, Criteria crit) {
        String useOrder = order;
        boolean useDesc = desc;

        if (lr != null && lr.getOrder() != null && lr.isDescending() != null) {
            useOrder = lr.getOrder();
            useDesc = lr.isDescending();
        }

        crit.addOrder(useDesc ? Order.desc(useOrder) : Order.asc(useOrder));

        if (lr != null && lr.getFirst() != null && lr.getLimit() != null) {
            crit.setFirstResult(lr.getFirst());
            crit.setMaxResults(lr.getLimit());
        }
    }

}
