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
package org.shredzone.cilla.core.search;

import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.text.filter.ParagraphFilter;
import org.shredzone.commons.text.filter.SimplifyHtmlFilter;
import org.shredzone.commons.text.filter.StripHtmlFilter;
import org.shredzone.commons.text.filter.TextileFilter;

/**
 * A simple formatter that converts {@link FormattedText} into plain text without markup.
 *
 * @author Richard "Shred" Körber
 */
public final class PlainTextFormatter {

    private static final ParagraphFilter PARAGRAPH_FILTER = new ParagraphFilter();
    private static final SimplifyHtmlFilter HTML_SIMPLIFY_FILTER = new SimplifyHtmlFilter();
    private static final StripHtmlFilter STRIP_HTML_FILTER = new StripHtmlFilter();
    private static final TextileFilter TEXTILE_FILTER = new TextileFilter();

    private PlainTextFormatter() {
        // Utility class without constructor
    }

    /**
     * Formats the given {@link FormattedText}.
     *
     * @param text
     *            {@link FormattedText} to format
     * @return formatted plain text
     */
    public static CharSequence format(FormattedText text) {
        if (text == null) {
            return null;
        }
        return format(text.getText(), text.getFormat());
    }

    /**
     * Formats the given formatted text
     *
     * @param text
     *            formatted text
     * @param format
     *            {@link TextFormat} the text is formatted with
     * @return formatted plain text
     */
    public static CharSequence format(CharSequence text, TextFormat format) {
        if (text == null || format == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(text);

        switch (format) {
            case HTML:
                sb = STRIP_HTML_FILTER.filter(sb);
                break;

            case PLAIN:
                break;

            case SIMPLIFIED:
                sb = HTML_SIMPLIFY_FILTER.filter(sb);
                sb = PARAGRAPH_FILTER.filter(sb);
                sb = STRIP_HTML_FILTER.filter(sb);
                break;

            case PARAGRAPHED:
                sb = PARAGRAPH_FILTER.filter(sb);
                sb = STRIP_HTML_FILTER.filter(sb);
                break;

            case PREFORMATTED:
                break;

            case TEXTILE:
                sb = TEXTILE_FILTER.filter(sb);
                sb = STRIP_HTML_FILTER.filter(sb);
                break;

            default:
                throw new IllegalArgumentException("Cannot handle format " + format);
        }

        return sb;
    }

}
