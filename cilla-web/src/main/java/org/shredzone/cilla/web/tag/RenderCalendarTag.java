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
package org.shredzone.cilla.web.tag;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.TagSupport;

import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.jshred.spring.taglib.annotation.Tag;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * A calendar table is rendered.
 *
 * @author Richard "Shred" Körber
 * @deprecated Will be replaced by a tag that uses fragments
 */
@Tag(type = IterationTag.class)
@TagInfo("A calendar table is rendered.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Deprecated
public class RenderCalendarTag extends TagSupport {
    private static final long serialVersionUID = 1864249497836934674L;

    private @TagParameter SearchResult result;

    private @Resource PageDao pageDao;
    private @Resource LinkService linkService;

    private DateRange date;
    private FilterModel filter;
    private Locale locale;
    private Calendar calendar;
    private String[] weekdays;
    private String[] months;
    private Set<Integer> pageDays;

    private int calMonth, calYear;
    private int prevMonth, prevYear;
    private int nextMonth, nextYear;

    private int toDay;

    @Override
    public int doEndTag() throws JspException {
        try {
            setupLocale();

            filter = new FilterModel();
            date = new DateRange();

            if (result != null) {
                filter = result.getFilter();
            }

            if (filter.getDate() != null) {
                date = filter.getDate();
            }

            setupCalendarDays();
            fetchPageDays();
            setupPrevNextMonth();
        } catch (CillaServiceException ex) {
            throw new JspException(ex);
        }

        try {
            JspWriter out = pageContext.getOut();

            out.append("<table class=\"aCalendar\">");
            writeCaption(out);
            writeHeadline(out);
            writeCalendar(out);
            out.append("</table>");
        } catch (IOException ex) {
            throw new JspException(ex);
        }

        return EVAL_PAGE;
    }

    /**
     * Sets up the {@link Locale} and {@link Calendar} settings depending on the visitors
     * request.
     */
    private void setupLocale() {
        locale = pageContext.getRequest().getLocale();
        calendar = Calendar.getInstance(locale);

        DateFormatSymbols symbols = DateFormatSymbols.getInstance(locale);
        weekdays = symbols.getShortWeekdays();
        months = symbols.getMonths();
    }

    /**
     * Sets up calendar days.
     */
    private void setupCalendarDays() {
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
    }

    /**
     * Fetch the set of page days for the current calendar.
     */
    private void fetchPageDays() throws CillaServiceException {
        if (result != null) {
            Calendar cal = Calendar.getInstance(locale);
            cal.clear();
            cal.set(calYear, calMonth - 1, 1);
            pageDays = result.fetchPageDays(cal);
        } else {
            pageDays = Collections.emptySet();
        }
    }

    /**
     * Sets up the previous and next calendar month.
     */
    private void setupPrevNextMonth() {
        Calendar cal = Calendar.getInstance(locale);
        cal.clear();
        cal.set(calYear, calMonth - 1, 1);

        Date[] prevNext = pageDao.fetchPrevNextMonth(cal);

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
     * Writes the caption line. It consists of the month name, the year, and links to the
     * previous and next month.
     *
     * @param out
     *            {@link Appendable} to write to
     */
    private void writeCaption(Appendable out) throws IOException {
        out.append("<caption class=\"aCalCaption\">");

        out.append("<span class=\"aCalCaptionPrevious\">");
        writePreviousLink(out);
        out.append("</span><span class=\"aCalCaptionMonth\">");
        out.append(months[calMonth - 1]);
        out.append(' ');
        out.append(String.valueOf(calYear));
        out.append("</span><span class=\"aCalCaptionNext\">");
        writeNextLink(out);
        out.append("</span>");

        out.append("</caption>");
    }

    /**
     * Writes the link to the previous month's calendar sheet.
     *
     * @param out
     *            {@link Appendable} to write to
     */
    private void writePreviousLink(Appendable out) throws IOException {
        if (prevYear > 0) {
            FilterModel clonedFilter = new FilterModel(filter);
            DateRange dm = new DateRange();
            dm.setYear(prevYear);
            dm.setMonth(prevMonth);
            dm.setDay(0);
            clonedFilter.setDate(dm);

            out.append("<a href=\"");
            out.append(linkService.linkTo().ref(clonedFilter).toString());
            out.append("\">");
            out.append("<img src=\"/img/cal-back.png\" width=\"16\" height=\"13\" alt=\"&lt;--\" />");
            out.append("</a>");
        }
    }

    /**
     * Writes the link to the next month's calendar sheet.
     *
     * @param out
     *            {@link Appendable} to write to
     */
    private void writeNextLink(Appendable out) throws IOException {
        if (nextYear > 0) {
            FilterModel clonedFilter = new FilterModel(filter);
            DateRange dm = new DateRange();
            dm.setYear(nextYear);
            dm.setMonth(nextMonth);
            dm.setDay(0);
            clonedFilter.setDate(dm);

            out.append("<a href=\"");
            out.append(linkService.linkTo().ref(clonedFilter).toString());
            out.append("\">");
            out.append("<img src=\"/img/cal-fwd.png\" width=\"16\" height=\"13\" alt=\"--&gt;\" />");
            out.append("</a>");
        }
    }

    /**
     * Writes the headline of the calendar table. It consists of the weekdays.
     *
     * @param out
     *            {@link Appendable} to write to
     */
    private void writeHeadline(Appendable out) throws IOException {
        out.append("<tr class=\"aCalRowWeekday\">");
        int firstDay = calendar.getFirstDayOfWeek();
        for (int wd = firstDay; wd < firstDay + 7; wd++) {
            int weekdayIndex = (wd - 1) % 7;

            String wdname = weekdays[weekdayIndex + 1];
            if (wdname.length() > 2) {
                wdname = wdname.substring(0, 2);
            }

            out.append("<th class=\"aCalWeekday aCalWday");
            out.append(String.valueOf(weekdayIndex));
            out.append("\">");
            out.append(HtmlUtils.htmlEscape(wdname));
            out.append("</th>");
        }
        out.append("</tr>");
    }

    /**
     * Writes the calendar table itself.
     *
     * @param out
     *            {@link Appendable} to write to
     */
    private void writeCalendar(Appendable out) throws IOException {
        Calendar cal = Calendar.getInstance(locale);
        cal.clear();
        cal.set(Calendar.YEAR, calYear);
        cal.set(Calendar.MONTH, calMonth - 1);
        cal.set(Calendar.DATE, 1);

        int firstDay = cal.getFirstDayOfWeek() - 1;
        int firstWeekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int numberOfDays = cal.getActualMaximum(Calendar.DATE);
        int skipDays = (firstWeekday + 7 - firstDay) % 7;

        out.append("<tr class=\"aCalRow aCalRowOdd\">");

        int row = 0;
        int column = 0;
        for (int ix = -skipDays; ix < numberOfDays; ix++) {
            if (ix >= 0) {
                if (pageDays != null && pageDays.contains(ix + 1)) {
                    writeLinkedDay(out, ix + 1);
                } else {
                    writeDay(out, ix + 1);
                }
            } else {
                writeEmptyDay(out);
            }

            column++;
            if (column == 7) {
                row++;
                column = 0;
                out.append("</tr>");
                out.append("<tr class=\"aCalRow");
                if (row % 2 == 0) {
                    out.append(" aCalRowOdd");
                }
                out.append("\">");
            }

        }

        for (; column < 7; column++) {
            writeEmptyDay(out);
        }

        out.append("</tr>");
    }

    /**
     * Generates a single date cell of the calendar table.
     *
     * @param out
     *            {@link Appendable} to write to
     * @param day
     *            Day to be written (1..31)
     */
    private void writeDay(Appendable out, int day) throws IOException {
        out.append("<td class=\"aCalDay");
        if (day == toDay) out.append(" aCalToday");
        if (calYear == date.getYear() && calMonth == date.getMonth() && day == date.getDay()) {
            out.append(" aCalBrowsed");
        }
        out.append("\">");
        out.append(String.valueOf(day));
        out.append("</td>");
    }

    /**
     * Generates a single date cell of the calendar table.
     *
     * @param out
     *            {@link Appendable} to write to
     * @param day
     *            Day to be written (1..31)
     */
    private void writeLinkedDay(Appendable out, int day) throws IOException {
        FilterModel clonedFilter = new FilterModel(filter);
        DateRange dm = new DateRange();
        dm.setYear(calYear);
        dm.setMonth(calMonth);
        dm.setDay(day);
        clonedFilter.setDate(dm);

        out.append("<td class=\"aCalDay aCalLinked");
        if (day == toDay) out.append(" aCalToday");
        if (calYear == date.getYear() && calMonth == date.getMonth() && day == date.getDay()) {
            out.append(" aCalBrowsed");
        }
        out.append("\">");
        out.append("<a href=\"");
        out.append(linkService.linkTo().ref(clonedFilter).toString());
        out.append("\">");
        out.append(String.valueOf(day));
        out.append("</a>");
        out.append("</td>");
    }

    /**
     * Generates a single empty date cell.
     *
     * @param out
     *            {@link Appendable} to write to
     */
    private void writeEmptyDay(Appendable out) throws IOException {
        out.append("<td class=\"aCalNoday\">");
        out.append("</td>");
    }

}
