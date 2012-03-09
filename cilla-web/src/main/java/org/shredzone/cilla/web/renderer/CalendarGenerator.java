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
package org.shredzone.cilla.web.renderer;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * Generates a HTML calendar by using a {@link CalendarRenderStrategy}.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalendarGenerator {

    private @Resource LinkService linkService;
    private @Resource PageDao pageDao;

    private HttpServletResponse resp;
    private Locale locale;
    private DateRange date;
    private FilterModel filter;
    private Set<Integer> pageDays;
    private int calMonth, calYear;
    private int prevMonth, prevYear;
    private int nextMonth, nextYear;
    private int toDay;

    /**
     * {@link HttpServletResponse} for generating links.
     * <p>
     * Must be invoked.
     */
    public void setHttpServletResponse(HttpServletResponse resp) { this.resp = resp; }

    /**
     * {@link Locale} to generate the calendar for.
     * <p>
     * Must be invoked.
     */
    public void setLocale(Locale locale)        { this.locale = locale; }
    public Locale getLocale()                   { return locale; }

    /**
     * Sets the {@link SearchResult} to create a calendar for.
     * <p>
     * Must be invoked after {@link #setLocale(Locale)}.
     *
     * @param result
     *            {@link SearchResult}, may be {@code null} for the current calendar
     */
    public void setSearchResult(SearchResult result) throws CillaServiceException {
        filter = new FilterModel();
        date = new DateRange();
        pageDays = Collections.emptySet();

        if (result != null) {
            filter = result.getFilter();
            if (filter.getDate() != null) {
                date = filter.getDate();
            } else {
                date = new DateRange();
            }
        }

        setup();

        if (result != null) {
            Calendar cal = Calendar.getInstance(locale);
            cal.clear();
            cal.set(calYear, calMonth - 1, 1);
            pageDays = result.fetchPageDays(cal);
        }
    }

    /**
     * Gets the start date of the calendar that is to be displayed.
     */
    public Calendar getDisplayCalendar() {
        Calendar cal = Calendar.getInstance(locale);
        cal.clear();
        cal.set(calYear, calMonth - 1, 1);
        return cal;
    }

    /**
     * Checks if the displayed calendar shows the first month of the blog. This means that
     * there is no previous link.
     */
    public boolean isFirstMonth() {
        return prevYear == 0;
    }

    /**
     * Checks if the displayed calendar shows the last month of the blog. This means that
     * there is no next link.
     */
    public boolean isLastMonth() {
        return nextYear == 0;
    }

    /**
     * Returns {@code true} if the given day of the displayed calendar is the day that has
     * been selected by the {@link SearchResult} filter.
     *
     * @param day
     *            Calendar day of the current calendar, starting from 1
     */
    public boolean isSelectedDay(int day) {
        return calYear == date.getYear() && calMonth == date.getMonth() && day == date.getDay();
    }

    /**
     * Returns {@code true} if the given day of the displayed calendar is today.
     *
     * @param day
     *            Calendar day of the current calendar, starting from 1
     */
    public boolean isToday(int day) {
        return toDay != 0 && day == toDay;
    }

    /**
     * Returns the URL of the previous calendar page.
     *
     * @return URL of the previous calendar, or {@code null} if it's the first month.
     */
    public String getPreviousUrl() {
        if (isFirstMonth()) {
            return null;
        }

        FilterModel clonedFilter = new FilterModel(filter);
        DateRange dm = new DateRange();
        dm.setYear(prevYear);
        dm.setMonth(prevMonth);
        dm.setDay(0);
        clonedFilter.setDate(dm);

        return getUrl(clonedFilter);
    }

    /**
     * Returns the URL of the next calendar page.
     *
     * @return URL of the next calendar, or {@code null} if it's the last month.
     */
    public String getNextUrl() {
        if (isLastMonth()) {
            return null;
        }

        FilterModel clonedFilter = new FilterModel(filter);
        DateRange dm = new DateRange();
        dm.setYear(nextYear);
        dm.setMonth(nextMonth);
        dm.setDay(0);
        clonedFilter.setDate(dm);

        return getUrl(clonedFilter);
    }

    /**
     * Returns the URL of the calendar day.
     *
     * @param day
     *            Calendar day of the current calendar, starting from 1
     * @return URL of that day, or {@code null} if there are no blog articles on that day
     */
    public String getDayUrl(int day) {
        if (!pageDays.contains(day)) {
            return null;
        }

        FilterModel clonedFilter = new FilterModel(filter);
        DateRange dm = new DateRange();
        dm.setYear(calYear);
        dm.setMonth(calMonth);
        dm.setDay(day);
        clonedFilter.setDate(dm);

        return getUrl(clonedFilter);
    }

    /**
     * Writes the calendar to the given output, using the given
     * {@link CalendarRenderStrategy}.
     *
     * @param strategy
     *            {@link CalendarRenderStrategy} for generating the HTML code
     * @param out
     *            output to write the HTML to
     */
    public void write(CalendarRenderStrategy strategy, Appendable out) throws IOException {
        strategy.write(this, out);
    }

    /**
     * Sets up the generator.
     */
    private void setup() {
        Calendar cal = Calendar.getInstance(locale);
        int year  = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day   = cal.get(Calendar.DATE);

        if (date.getYear() > 0 && date.getMonth() > 0) {
            calYear  = date.getYear();
            calMonth = date.getMonth();
            if (calYear == year && calMonth == month) {
                toDay = day;
            } else {
                toDay = 0;
            }
        } else {
            calYear  = year;
            calMonth = month;
            toDay    = day;
        }

        Date[] prevNext = pageDao.fetchPrevNextMonth(getDisplayCalendar());

        if (prevNext[0] != null) {
            cal.setTime(prevNext[0]);
            prevYear = cal.get(Calendar.YEAR);
            prevMonth = cal.get(Calendar.MONTH) + 1;
        } else {
            prevYear = 0;
        }

        if (prevNext[1] != null) {
            cal.setTime(prevNext[1]);
            nextYear = cal.get(Calendar.YEAR);
            nextMonth = cal.get(Calendar.MONTH) + 1;
        } else {
            nextYear = 0;
        }
    }

    /**
     * Creates the URL to a date page.
     *
     * @param filter
     *            {@link FilterModel} for the contents
     * @return URL of the page
     */
    private String getUrl(FilterModel filter) {
        LinkBuilder lb = linkService.linkTo().ref(filter);
        return HtmlUtils.htmlEscape(resp.encodeURL(lb.toString()));
    }

}
