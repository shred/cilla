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

import org.shredzone.cilla.service.search.renderer.SimpleSearchResultRenderer;
import org.springframework.stereotype.Component;

/**
 * Renders a search result.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class DefaultSearchResultRenderer extends SimpleSearchResultRenderer {

    @Override
    public int getMaxResults() {
        return 10;
    }

    @Override
    public String getHeader() {
        return "<p>";
    }

    @Override
    public String getSeparator() {
        return "...</p><p>";
    }

    @Override
    public String getFooter() {
        return "</p>";
    }

    @Override
    public String getSpanPre() {
        return "<strong>";
    }

    @Override
    public String getSpanPost() {
        return "</strong>";
    }

}
