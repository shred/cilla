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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A single right.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "authority.all", query = "FROM Authority ORDER BY name"),
    @NamedQuery(name = "authority.byName", query = "FROM Authority WHERE name=:name"),
})
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Authority extends BaseModel {
    private static final long serialVersionUID = 1354398537688121518L;

    private String name;

    /**
     * Descriptive name of the right.
     */
    @Column(unique = true, nullable = false)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Authority && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
