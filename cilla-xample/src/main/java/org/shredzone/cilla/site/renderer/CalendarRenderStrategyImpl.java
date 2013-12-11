/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2013 Richard "Shred" Körber
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
package org.shredzone.cilla.site.renderer;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.Calendar;

import org.shredzone.cilla.web.renderer.CalendarGenerator;
import org.shredzone.cilla.web.renderer.CalendarRenderStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * A {@link CalendarRenderStrategy} that generates a simple HTML calendar.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CalendarRenderStrategyImpl implements CalendarRenderStrategy {

    protected CalendarGenerator generator;
    protected Appendable out;

    @Override
    public void write(CalendarGenerator generator, Appendable out) throws IOException {
        this.generator = generator;
        this.out = out;

        writeHeader();
        writeCaption();
        writeHeadline();
        writeCalendar();
        writeFooter();
    }

    /**
     * Writes out the calendar header.
     */
    protected void writeHeader() throws IOException {
        out.append("<table>");
    }

    /**
     * Writes out the calendar footer.
     */
    protected void writeFooter() throws IOException {
        out.append("</table>");
    }

    /**
     * Writes the caption line. It consists of the month name, the year, and links to the
     * previous and next month.
     */
    protected void writeCaption() throws IOException {
        Calendar cal = generator.getDisplayCalendar();

        DateFormatSymbols symbols = DateFormatSymbols.getInstance(generator.getLocale());
        String[] months = symbols.getMonths();

        out.append("<caption>");
        writePreviousLink();
        out.append(months[cal.get(Calendar.MONTH)]);
        out.append(' ');
        out.append(String.valueOf(cal.get(Calendar.YEAR)));
        writeNextLink();
        out.append("</caption>");
    }

    /**
     * Writes the link to the previous month's calendar sheet.
     */
    protected void writePreviousLink() throws IOException {
        if (!generator.isFirstMonth()) {
            out.append("<a href=\"").append(generator.getPreviousUrl()).append("\">");
            out.append("&lt;&mdash;&nbsp;"); // Arrow left
            out.append("</a>");
        }
    }

    /**
     * Writes the link to the next month's calendar sheet.
     */
    protected void writeNextLink() throws IOException {
        if (!generator.isLastMonth()) {
            out.append("<a href=\"").append(generator.getNextUrl()).append("\">");
            out.append("&nbsp;&mdash;&gt;"); // Arrow right
            out.append("</a>");
        }
    }

    /**
     * Writes the headline of the calendar table. It consists of the weekdays.
     */
    protected void writeHeadline() throws IOException {
        Calendar calendar = Calendar.getInstance(generator.getLocale());
        int firstDay = calendar.getFirstDayOfWeek();

        DateFormatSymbols symbols = DateFormatSymbols.getInstance(generator.getLocale());
        String[] weekdays = symbols.getShortWeekdays();

        out.append("<tr>");
        for (int wd = firstDay; wd < firstDay + 7; wd++) {
            int weekdayIndex = (wd - 1) % 7;

            String wdname = weekdays[weekdayIndex + 1];
            if (wdname.length() > 2) {
                wdname = wdname.substring(0, 2);
            }

            out.append("<th>");
            out.append(HtmlUtils.htmlEscape(wdname));
            out.append("</th>");
        }
        out.append("</tr>");
    }

    /**
     * Writes the calendar table itself.
     */
    protected void writeCalendar() throws IOException {
        Calendar cal = generator.getDisplayCalendar();

        int firstDay = cal.getFirstDayOfWeek() - 1;
        int firstWeekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
        int numberOfDays = cal.getActualMaximum(Calendar.DATE);
        int skipDays = (firstWeekday + 7 - firstDay) % 7;

        out.append("<tr>");

        int column = 0;
        for (int ix = -skipDays; ix < numberOfDays; ix++) {
            if (ix >= 0) {
                String url = generator.getDayUrl(ix + 1);
                if (url != null) {
                    writeLinkedDay(ix + 1, url);
                } else {
                    writeDay(ix + 1);
                }
            } else {
                writeEmptyDay();
            }

            column++;
            if (column == 7) {
                column = 0;
                out.append("</tr>");
                out.append("<tr>");
            }

        }

        for (; column < 7; column++) {
            writeEmptyDay();
        }

        out.append("</tr>");
    }

    /**
     * Generates a single date cell of the calendar table.
     *
     * @param day
     *            Day to be written (1..31)
     */
    protected void writeDay(int day) throws IOException {
        out.append("<td>");
        if (generator.isSelectedDay(day)) {
            out.append("<b>");
        }
        out.append(String.valueOf(day));
        if (generator.isSelectedDay(day)) {
            out.append("</b>");
        }
        out.append("</td>");
    }

    /**
     * Generates a single date cell of the calendar table.
     *
     * @param day
     *            Day to be written (1..31)
     * @param url
     *            URL to link to
     */
    protected void writeLinkedDay(int day, String url) throws IOException {
        out.append("<td>");
        out.append("<a href=\"").append(url).append("\">");
        if (generator.isSelectedDay(day)) {
            out.append("<b>");
        }
        out.append(String.valueOf(day));
        if (generator.isSelectedDay(day)) {
            out.append("</b>");
        }
        out.append("</a>");
        out.append("</td>");
    }

    /**
     * Generates a single empty date cell.
     */
    protected void writeEmptyDay() throws IOException {
        out.append("<td>");
        out.append("</td>");
    }

}
