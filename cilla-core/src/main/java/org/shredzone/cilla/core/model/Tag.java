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

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A single tag.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tag extends BaseModel {
    private static final long serialVersionUID = 6244259856286790454L;

    private String name;

    /**
     * Tag name.
     */
    @Column(nullable = false, unique = true)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

}
