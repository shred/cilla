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
import java.util.Locale;
import java.util.ResourceBundle;

import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.web.renderer.AbstractPaginatorRendererStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Renders a paginator.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PaginatorStrategyImpl extends AbstractPaginatorRendererStrategy {

    // use even numbers for best results!
    private static final int maxPageGroupSize = 8;

    private ResourceBundle bundle;
    private boolean needsSeparator;

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    @Override
    public void openContainer(Appendable out) throws IOException {
        out.append("<div>");
    }

    @Override
    public void closeContainer(Appendable out) throws IOException {
        out.append("</div>");
    }

    @Override
    public void previousLink(Appendable out, String url) throws IOException {
        if (url != null) {
            out.append("<a class=\"button\"");
            out.append(" href=\"").append(url).append("\">");
            out.append(bundle.getString("paginator.previous"));
            out.append("</a>");
        }
    }

    @Override
    public void nextLink(Appendable out, String url) throws IOException {
        if (url != null) {
            out.append("<a class=\"button\"");
            out.append(" href=\"").append(url).append("\">");
            out.append(bundle.getString("paginator.next"));
            out.append("</a>");
        }
    }

    @Override
    public void pageLink(Appendable out, String url, int current, int selected, int last) throws IOException {
        if (needsSeparator) {
            out.append("&middot;&middot;&middot;");
            needsSeparator = false;
        }

        if (current == selected) {
            out.append("<span class=\"button button-disabled\"><b>");
            out.append(String.valueOf(current + 1));
            out.append("</b></span>&nbsp;");
        } else {
            out.append("<a class=\"button\" href=\"").append(url).append("\">");
            out.append(String.valueOf(current + 1));
            out.append("</a>&nbsp;");
        }
    }

    @Override
    public int computeNextPage(PaginatorModel model, int current) {
        int lastPage = model.getPageCount() - 1;
        int start = Math.max(model.getSelectedPage() - (maxPageGroupSize / 2), 0);
        int end = Math.min(start + maxPageGroupSize, lastPage);
        start = Math.max(end - maxPageGroupSize, 0);

        if (current < start) {
            needsSeparator = true;
            return start;
        } else if (current >= end) {
            needsSeparator = true;
            return lastPage;
        } else {
            needsSeparator = false;
            return current + 1;
        }
    }

}
