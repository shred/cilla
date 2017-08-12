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

import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.web.fragment.annotation.FragmentValue;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * A fragment renderer for paginators. Requires a bean that implements the
 * {@link PaginatorRendererStrategy}.
 *
 * @author Richard "Shred" Körber
 */
@Component
@FragmentRenderer
public class PaginatorFragmentRenderer {

    private @Resource ApplicationContext applicationContext;
    private @Resource LinkService linkService;

    @Fragment(name = "paginator")
    public void paginatorFragment(
        PaginatorModel model,
        @FragmentValue("#filter") FilterModel filter,
        Locale locale,
        HttpServletResponse resp,
        JspWriter out
    ) throws IOException {
        int selected = model.getSelectedPage();

        // Setup the PaginatorStrategy
        PaginatorRendererStrategy strategy = getPaginatorStrategy();
        strategy.setLocale(locale);

        // Open the paginator container
        strategy.openContainer(out);

        // "previous" link
        if (model.isFirstPage()) {
            strategy.previousLink(out, null);
        } else {
            strategy.previousLink(out, getUrl(resp, filter, selected - 1));
        }

        // page links
        int current = 0, last;
        do {
            strategy.pageLink(out, getUrl(resp, filter, current), current, selected);
            last = current;
            current = strategy.computeNextPage(model, current);
        } while (current >= 0 && current != last);

        // "next" link
        if (model.isLastPage()) {
            strategy.nextLink(out, null);
        } else {
            strategy.nextLink(out, getUrl(resp, filter, selected + 1));
        }

        // Close the paginator container
        strategy.closeContainer(out);
        out.println();
    }

    /**
     * Creates the URL to a page of the paginator.
     *
     * @param resp
     *            {@link HttpServletResponse} for creating the link
     * @param filter
     *            {@link FilterModel} for the contents
     * @param page
     *            page number
     * @return URL of the page
     */
    private String getUrl(HttpServletResponse resp, FilterModel filter, int page) {
        LinkBuilder lb = linkService.linkTo().ref(filter);
        if (page != 0) {
            lb.query("p", String.valueOf(page));
        }
        return HtmlUtils.htmlEscape(resp.encodeURL(lb.toString()));
    }

    /**
     * Gets a new {@link PaginatorRendererStrategy}.
     */
    protected PaginatorRendererStrategy getPaginatorStrategy() {
        return applicationContext.getBean(PaginatorRendererStrategy.class);
    }

}
