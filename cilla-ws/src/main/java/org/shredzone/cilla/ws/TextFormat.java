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
package org.shredzone.cilla.ws;

/**
 * An enumeration of formats a text can be formatted in.
 *
 * @author Richard "Shred" Körber
 */
public enum TextFormat {

    /*
     * IMPORTANT NOTE: ALWAYS APPEND NEW ENTRIES TO THE END! Never change the sequence,
     * delete entries, or insert entries, since the TextFormat choice is persisted by its
     * index, not its name.
     */

    /**
     * Plain text. HTML characters are escaped. Newlines are converted to line breaks.
     */
    PLAIN(false),

    /**
     * Preformatted text. HTML characters are escaped. A monospaced character set is used.
     * Newlines are converted to line breaks.
     */
    PREFORMATTED(false),

    /**
     * Simplified HTML. Only a few harmless tags are accepted, the rest is escaped.
     * Newlines are converted to line breaks.
     */
    SIMPLIFIED(false),

    /**
     * Full HTML, with newlines converted to line breaks. Potentially harmful!
     */
    PARAGRAPHED(true),

    /**
     * Full HTML. The text is rendered without modifications. Potentially harmful!
     */
    HTML(true),

    /**
     * Textile formatting.
     */
    TEXTILE(false);

    private final boolean harmful;

    private TextFormat(boolean harmful) {
        this.harmful = harmful;
    }

    /**
     * If {@code true}, this {@link TextFormat} is potentially harmful. For example, it
     * would allow XSS. It is strongly recommended not to use any harmful
     * {@link TextFormat} for text from dubious sources (like anonymous comments).
     *
     * @return {@code true}: TextFormat is potentially harmful. {@code false}: TextFormat
     *         is considered safe even for external texts.
     */
    public boolean isHarmful() {
        return harmful;
    }

}
