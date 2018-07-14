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
package org.shredzone.cilla.core.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * A section that embeds an external page.
 *
 * @author Richard "Shred" Körber
 */
@Entity
public class EmbedSection extends Section {
    private static final long serialVersionUID = 32319753941354062L;

    private String embedUrl;

    @Column(nullable = false)
    public String getEmbedUrl()                 { return embedUrl; }
    public void setEmbedUrl(String embedUrl)    { this.embedUrl = embedUrl; }

    @Override
    @Transient
    public String getType()                     { return "embed"; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof EmbedSection && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
