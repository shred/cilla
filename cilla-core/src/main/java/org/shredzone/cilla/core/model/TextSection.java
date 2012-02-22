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
package org.shredzone.cilla.core.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.shredzone.cilla.core.model.embed.FormattedText;

/**
 * A section that contains a text.
 *
 * @author Richard "Shred" Körber
 * @version $Revision:$
 */
@Entity
public class TextSection extends Section {
    private static final long serialVersionUID = 65044482849900703L;

    private FormattedText text;

    /**
     * The text.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "text", column = @Column(name = "text", nullable = false)),
        @AttributeOverride(name = "format", column = @Column(name = "textFormat", nullable = false))
    })
    public FormattedText getText()              { return text; }
    public void setText(FormattedText text)     { this.text = text; }

    @Override
    @Transient
    public String getType()                     { return "text"; }

}
