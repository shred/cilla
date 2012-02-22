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
package org.shredzone.cilla.core.model.embed;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

import org.hibernate.annotations.Type;
import org.shredzone.cilla.ws.TextFormat;

/**
 * A text and its formatting mode.
 *
 * @author Richard "Shred" Körber
 */
@Embeddable
public class FormattedText implements Serializable {
    private static final long serialVersionUID = -5078620523618106527L;

    private TextFormat format;
    private String text;

    /**
     * Instantiates an empty formatted text.
     */
    public FormattedText() {
        // Default constructor
    }

    /**
     * Instantiates a new formatted text.
     *
     * @param text
     *            the text
     * @param format
     *            the format
     */
    public FormattedText(String text, TextFormat format) {
        this.text = text;
        this.format = format;
    }

    /**
     * The format mode.
     */
    @Enumerated(EnumType.ORDINAL)
    public TextFormat getFormat()               { return format; }
    public void setFormat(TextFormat format)    { this.format = format; }

    /**
     * The formatted text.
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String getText()                     { return text; }
    public void setText(String text)            { this.text = text; }

}
