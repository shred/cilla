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
package org.shredzone.cilla.web.plugin;

import org.shredzone.cilla.web.plugin.annotation.Priority;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.text.TextFilter;

/**
 * A {@link TextFilter} that is invoked for post-processing formatted text.
 * <p>
 * To implement a {@link PostProcessingTextFilter}, just register it as a Spring bean.
 * Use {@link Priority} annotation to define the order of the filters to be executed.
 * <p>
 * A {@link StringBuilder} is passed to the filter. It contains formatted HTML, and may
 * either modify the {@link StringBuilder}, or create a new one.
 * <p>
 * {@link TextFormat#HTML} and {@value TextFormat#PREFORMATTED} are not post-processed.
 *
 * @author Richard "Shred" Körber
 */
public interface PostProcessingTextFilter extends TextFilter {

}
