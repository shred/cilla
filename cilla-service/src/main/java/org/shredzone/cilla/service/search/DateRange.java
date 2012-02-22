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
package org.shredzone.cilla.service.search;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * A date range spans either an entire year, an entire month or a day.
 *
 * @author Richard "Shred" Körber
 */
public class DateRange implements Serializable {
    private static final long serialVersionUID = 6545844612991815036L;

    private int year, month, day;

    /**
     * Creates an empty date range.
     */
    public DateRange() {
        // Default constructor
    }

    /**
     * Clone constructor.
     *
     * @param range
     *            {@link DateRange} to be cloned
     */
    public DateRange(DateRange range) {
        this.year = range.year;
        this.month = range.month;
        this.day = range.day;
    }

    /**
     * Creates a {@link DateRange} by parsing a string. Valid strings are e.g.:
     * <ul>
     * <li><tt>2012-02-01</tt>: a day (February 1st, 2012)</li>
     * <li><tt>2012-02</tt>: a month (February 2012)</li>
     * <li><tt>2012</tt>: a year (2012)</li>
     * <li>Empty string or {@code null}: empty date range</li>
     * </ul>
     *
     * @param string
     *            String to be parsed, may be {@code null}
     * @return {@link DateRange} containing the date
     * @throws IllegalArgumentException
     *             if the date is not valid
     */
    public static DateRange parse(String string) {
        DateRange result = new DateRange();

        int dateIndex = 0;

        for (String part : string.split("\\-+")) {
            try {
                int dayPart = Integer.parseInt(part);
                switch (dateIndex) {
                    case 0: result.setYear(dayPart); break;
                    case 1: result.setMonth(dayPart); break;
                    case 2: result.setDay(dayPart); break;
                    default: throw new IllegalArgumentException("too many date parts");
                }
                dateIndex++;
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(ex.getMessage());
            }
        }

        return result;
    }

    /**
     * Year to be shown, or 0 if not limited by year.
     */
    public int getYear()                            { return year; }
    public void setYear(int year) {
        if (year < 0) {
            throw new IllegalArgumentException("year must not be negative");
        }

        this.year = year;
    }

    /**
     * Month to be shown, or 0 if not limited by month. Months are counted
     * starting from 1. If no year is given, month will be ignored.
     */
    public int getMonth()                           { return month; }
    public void setMonth(int month) {
        if (month < 0 || month > 12) {
            throw new IllegalArgumentException("month out of range (0..12)");
        }

        this.month = month;
    }

    /**
     * Day to be shown, or 0 if not limited by day. If no year or no month is
     * given, day will be ignored.
     */
    public int getDay()                             { return day; }
    public void setDay(int day) {
        if (day < 0 || day > 31) {
            throw new IllegalArgumentException("day out of range (0..31)");
        }

        this.day = day;
    }

    /**
     * Checks if the Date Range is unset.
     */
    public boolean isEmpty() {
        return year == 0 && month == 0 && day == 0;
    }

    /**
     * Sets the day, month and year according to the given {@link Date}.
     *
     * @param date
     *            {@link Date} to set the calendar settings for
     */
    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        setYear(cal.get(Calendar.YEAR));
        setMonth(cal.get(Calendar.MONTH) + 1);
        setDay(cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Gets a {@link Calendar} representation of the date range.
     *
     * @return new {@link Calendar} instance
     */
    public Calendar getCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        if (year > 0) {
            cal.set(Calendar.YEAR, year);
            if (month > 0) {
                cal.set(Calendar.MONTH, month - 1);
                if (day > 0) {
                    cal.set(Calendar.DAY_OF_MONTH, day);
                }
            }
        }
        return cal;
    }

    /**
     * Gets the starting date of this date range. If the date range is empty, Epoch
     * (Januar 1st, 1970) is returned.
     *
     * @return new {@link Calendar} instance with the starting date
     */
    public Calendar getFromDate() {
        Calendar cal = getCalendar();
        if (day == 0) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            if (month == 0) {
                cal.set(Calendar.MONTH, 0);
                if (year == 0) {
                    cal.set(Calendar.YEAR, 1970);
                }
            }
        }
        return cal;
    }

    /**
     * Gets the ending date of this date range (inclusive). If the date range is empty,
     * December 31st of the current year is returned.
     *
     * @return new {@link Calendar} instance with the ending date (inclusive)
     */
    public Calendar getThruDate() {
        Calendar cal = getFromDate();
        if (day > 0) {
            cal.add(Calendar.DAY_OF_MONTH, 1);
        } else if (month > 0) {
            cal.add(Calendar.MONTH, 1);
        } else if (year > 0) {
            cal.add(Calendar.YEAR, 1);
        } else {
            int year = Calendar.getInstance().get(Calendar.YEAR);
            cal.set(Calendar.YEAR, year + 1);
        }
        cal.add(Calendar.MILLISECOND, -1);
        return cal;
    }

    /**
     * Compares two date ranges. They are equal if they are set to the very same time
     * span.
     *
     * @param obj
     *            Object to compare
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DateRange)) {
            return false;
        }

        DateRange cmp = (DateRange) obj;
        return cmp.day == day && cmp.month == month && cmp.year == year;
    }

    @Override
    public int hashCode() {
        return (((year << 4) + month) << 5) + day;
    }

    /**
     * Returns a string representation of this {@link DateRange}. The returned string can
     * be parsed with {@link #parse(String)}.
     *
     * @return String representation, or an empty string if this date range is unset
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (year > 0) {
            sb.append(year);
            if (month > 0) {
                sb.append('-').append(month);
                if (day > 0) {
                    sb.append('-').append(day);
                }
            }
        }

        return sb.toString();
    }

}
