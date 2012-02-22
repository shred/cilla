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
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;

/**
 * A simple {@link SearchResultRenderer} implementation that wraps away all Lucene stuff.
 *
 * @author Richard "Shred" Körber
 */
public abstract class SimpleSearchResultRenderer implements SearchResultRenderer {

    /**
     * Returns the HTML tag which is prepended to a highlight marker.
     */
    public abstract String getSpanPre();

    /**
     * Returns the HTML tag which is appended to a highlight marker.
     */
    public abstract String getSpanPost();

    @Override
    public Formatter createFormatter() {
        return new SimpleHTMLFormatter(getSpanPre(), getSpanPost());
    }

    @Override
    public Fragmenter createFragmenter(QueryScorer scorer) {
        return new SimpleSpanFragmenter(scorer);
    }

}
