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
package org.shredzone.cilla.web.format;

import java.util.function.Supplier;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.ws.TextFormat;

/**
 * Formats the given {@link FormattedText} into HTML.
 *
 * @author Richard "Shred" Körber
 */
public interface TextFormatter {

    /**
     * Formats the given {@link FormattedText}. Relative links are not resolved.
     *
     * @param text
     *            the {@link FormattedText} to be formatted
     * @return HTML
     */
    CharSequence format(FormattedText text);

    /**
     * Formats the given {@link FormattedText}. Relative links are related to the given
     * {@link Page}.
     *
     * @param text
     *            the {@link FormattedText} to be formatted
     * @param linkBuilderSupplier
     *            supplier for a preconfigured {@link LinkBuilder} to be used, or
     *            {@code null} to generate relative links
     * @return HTML
     */
    CharSequence format(FormattedText text, Supplier<LinkBuilder> linkBuilderSupplier);

    /**
     * Formats the given {@link CharSequence}. The text is formatted according to the
     * given {@link TextFormat}. Relative links are not resolved.
     *
     * @param text
     *            the text to be formatted
     * @param format
     *            the {@link TextFormat} the given text is in
     * @return HTML
     */
    CharSequence format(CharSequence text, TextFormat format);

    /**
     * Formats the given {@link CharSequence}. The text is formatted according to the
     * given {@link TextFormat}. Relative links are related to the given {@link Page}.
     *
     * @param text
     *            the text to be formatted
     * @param format
     *            the {@link TextFormat} the given text is in
     * @param linkBuilderSupplier
     *            supplier for a preconfigured {@link LinkBuilder} to be used, or
     *            {@code null} to generate relative links
     * @return HTML
     */
    CharSequence format(CharSequence text, TextFormat format, Supplier<LinkBuilder> linkBuilderSupplier);

    /**
     * Strips all html from the given string. Opening tags are replaced by a whitespace.
     *
     * @param html
     *            HTML string to be stripped
     * @return Plain text
     */
    CharSequence stripHtml(CharSequence html);

}
