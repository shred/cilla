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
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentItem;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * A fragment for rendering a calendar. If a {@link SearchResult} is given, the
 * appropriate page of the calendar is shown.
 *
 * @author Richard "Shred" Körber
 */
@Component
@FragmentRenderer
public class CalendarFragmentRenderer {

    private @Resource ApplicationContext applicationContext;

    /**
     * Renders a calendar.
     *
     * @param result
     *            {@link SearchResult}, today's calendar if {@code null}
     * @param locale
     *            {@link Locale} of the calendar output
     * @param resp
     *            {@link HttpServletResponse} for creating links
     * @param out
     *            {@link JspWriter} to write the calendar to
     */
    @Fragment(name = "calendar")
    public void calendarFragment(
        @FragmentItem SearchResult result,
        Locale locale,
        HttpServletResponse resp,
        JspWriter out
    ) throws CillaServiceException, IOException {
        CalendarGenerator generator = getCalendarGenerator();
        generator.setHttpServletResponse(resp);
        generator.setLocale(locale);
        generator.setSearchResult(result);

        CalendarRenderStrategy strategy = getCalendarRenderStrategy();
        generator.write(strategy, out);
    }

    /**
     * Gets a new {@link CalendarGenerator}.
     */
    protected CalendarGenerator getCalendarGenerator() {
        return applicationContext.getBean(CalendarGenerator.class);
    }

    /**
     * Gets a new {@link CalendarRenderStrategy}.
     */
    protected CalendarRenderStrategy getCalendarRenderStrategy() {
        return applicationContext.getBean(CalendarRenderStrategy.class);
    }

}
