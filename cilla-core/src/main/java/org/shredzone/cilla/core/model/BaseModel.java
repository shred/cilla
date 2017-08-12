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

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * Represents the superclass of all other entities. It contains the primary key and a
 * version for optimistic locking.
 *
 * @author Richard "Shred" Körber
 */
@MappedSuperclass
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 8792848272867709575L;

    private long id;
    private long version;

    /**
     * Primary key.
     */
    @Id
    @GeneratedValue
    public long getId()                         { return id; }
    public void setId(long id)                  { this.id = id; }

    /**
     * Version for optimistic locking.
     */
    @Version
    public long getVersion()                    { return version; }
    public void setVersion(long version)        { this.version = version; }

    /**
     * Compares two model entities. The entities are considered equal if they are of the
     * same type and contain the same primary key ID.
     *
     * @param obj
     *            Object to compare with
     * @return {@code true} if the entities are deemed to be equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BaseModel)) {
            return false;
        }

        return ((BaseModel) obj).getId() == this.getId();
    }

    /**
     * Creates a hash code for this model entity.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return (int) ((getId()>>32) ^ getId());
    }

}
