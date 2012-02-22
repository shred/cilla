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
package org.shredzone.cilla.core.util;

import java.util.Calendar;
import java.util.Date;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.ws.PageOrder;

/**
 * Utility class for date handling.
 *
 * @author Richard "Shred" Körber
 */
public final class DateUtils {

    private DateUtils() {
        // Utility class, not to be instanciated
    }

    /**
     * Fetches the order date of a page.
     *
     * @param page
     *            {@link Page} to get the date for
     * @param order
     *            {@link PageOrder} to be used
     * @return {@link Date} of the page. May be {@code null} for unpublished or expired
     *         pages.
     */
    public static Date getOrderDate(Page page, PageOrder order) {
        switch (order) {
            case CREATION: return page.getCreation();
            case MODIFICATION: return page.getModification();
            case PUBLICATION: return page.getPublication();
            default: throw new IllegalArgumentException("Unknown page order " + order);
        }
    }

    /**
     * Computes the beginning of a month of the given date. The returned {@link Calendar}
     * object is set to midnight of the first day of that month.
     *
     * @param cal
     *            {@link Calendar} to compute the beginning of month from
     * @return new {@link Calendar} object containing the beginning of month
     */
    public static Calendar beginningOfMonth(Calendar cal) {
        Calendar result = Calendar.getInstance(cal.getTimeZone());
        result.clear();
        result.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 1);
        return result;
    }

    /**
     * Computes the first day of next month of the given date. The returned
     * {@link Calendar} object is set to midnight of the first day of the next month.
     *
     * @param cal
     *            {@link Calendar} to compute the beginning of next month from
     * @return new {@link Calendar} object containing the beginning of next month
     */
    public static Calendar beginningOfNextMonth(Calendar cal) {
        Calendar result = beginningOfMonth(cal);
        result.add(Calendar.MONTH, 1);
        return result;
    }

    /**
     * Clones a {@link Date} object. Unlike {@link String}, {@link Date} objects are not
     * immutable. By cloning them, {@link Date} objects used as parameter or result value
     * cannot be manipulated from the outside.
     *
     * @param date
     *            {@link Date} to be cloned.
     * @return cloned {@link Date}, or {@code null} if {@code null} was passed in
     */
    public static Date cloneDate(Date date) {
        if (date != null) {
            return (Date) date.clone();
        } else {
            return null;
        }
    }

}
