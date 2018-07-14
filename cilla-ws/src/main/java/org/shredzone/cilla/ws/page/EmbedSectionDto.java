/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2018 Richard "Shred" Körber
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
package org.shredzone.cilla.ws.page;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An Embed Section.
 *
 * @author Richard "Shred" Körber
 */
@XmlRootElement
public class EmbedSectionDto extends SectionDto {
    private static final long serialVersionUID = 6845763957616970677L;

    private String embedUrl;

    @Override
    public String getType()                     { return "embed"; }

    @NotNull
    public String getEmbedUrl()                 { return embedUrl; }
    public void setEmbedUrl(String embedUrl)    { this.embedUrl = embedUrl; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof EmbedSectionDto && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
