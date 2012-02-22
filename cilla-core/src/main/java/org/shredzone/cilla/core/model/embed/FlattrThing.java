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

/**
 * Reference to a registered Flattr Thing.
 *
 * @author Richard "Shred" Körber
 */
@Embeddable
public class FlattrThing implements Serializable {
    private static final long serialVersionUID = -8002254641861809168L;

    private String flattrId;
    private String flattrUrl;

    /**
     * Flattr ID
     */
    public String getFlattrId()                 { return flattrId; }
    public void setFlattrId(String flattrId)    { this.flattrId = flattrId; }

    /**
     * Flattr URL
     */
    public String getFlattrUrl()                { return flattrUrl; }
    public void setFlattrUrl(String flattrUrl)  { this.flattrUrl = flattrUrl; }

}
