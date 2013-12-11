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

import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentItem;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;
import org.springframework.stereotype.Component;

/**
 * A Fragment Renderer for rendering a {@link Section}.
 *
 * @author Richard "Shred" Körber
 */
@FragmentRenderer
@Component
public class SectionFragmentRenderer {

    /**
     * Renders a {@link Section} completely.
     */
    @Fragment
    public void sectionFragment(
        @FragmentItem Section section,
        FragmentContext context
    ) throws IOException {
        context.setAttribute("section", section);
        context.include("section/" + section.getType() + "/index.jspf");
    }

    /**
     * Renders the short version of a {@link Section}.
     */
    @Fragment
    public void sectionShortFragment(
        @FragmentItem Section section,
        FragmentContext context
    ) throws IOException {
        context.setAttribute("section", section);
        context.include("section/" + section.getType() + "/short.jspf");
    }

}
