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

import java.io.Serializable;

/**
 * Contains the parameters of a list range and order.
 *
 * @author Richard "Shred" Körber
 */
public class ListRange implements Serializable {
    private static final long serialVersionUID = 3335159076274281094L;

    private String order;
    private Boolean descending;
    private Integer first;
    private Integer limit;

    /**
     * Property to order by. {@code null} for default property.
     */
    public void setOrder(String order)          { this.order = order; }
    public String getOrder()                    { return order; }

    /**
     * {@code true} for descending order. {@code null} for default order.
     */
    public void setDescending(Boolean descending) { this.descending = descending; }
    public Boolean isDescending()               { return descending; }

    /**
     * Number of the first row to return, or {@code null} for no limitation.
     */
    public void setFirst(Integer first)         { this.first = first; }
    public Integer getFirst()                   { return first; }

    /**
     * Maximum number of rows to return, or {@code null} for no limitation.
     */
    public void setLimit(Integer limit)         { this.limit = limit; }
    public Integer getLimit()                   { return limit; }

}
