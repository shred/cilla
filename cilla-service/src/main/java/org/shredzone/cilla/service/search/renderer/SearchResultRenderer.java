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
package org.shredzone.cilla.service.search.renderer;

import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.QueryScorer;

/**
 * Renders a search result.
 *
 * @author Richard "Shred" Körber
 */
public interface SearchResultRenderer {

    /**
     * Creates the Lucene {@link Formatter} for formatting the result.
     */
    Formatter createFormatter();

    /**
     * Creates the Lucene {@link Fragmenter} for fragmenting the text.
     */
    Fragmenter createFragmenter(QueryScorer scorer);

    /**
     * Returns the HTML header which is prepended to each search result.
     */
    String getHeader();

    /**
     * Returns the HTML separator which is placed between two fragments.
     */
    String getSeparator();

    /**
     * Returns the HTML footer which is appended to each search result.
     */
    String getFooter();

    /**
     * Returns the maximum number of shown fragments per page.
     */
    int getMaxResults();

}
