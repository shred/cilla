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

import java.io.Serializable;

/**
 * Base superclass for all DTOs.
 *
 * @author Richard "Shred" Körber
 */
public abstract class BaseDto implements Serializable {
    private static final long serialVersionUID = -4521178845073715686L;
    private static final long NEW_ID = 0L;

    private long id = NEW_ID;

    /**
     * A unique ID, or 0 for newly created DTOs.
     */
    public long getId()                         { return id; }
    public void setId(long id)                  { this.id = id; }

    /**
     * Checks if this DTO already has a persisted counterpart in the backend.
     *
     * @return {@code true} if this DTO was already persisted, {@code false} if it is
     *         newly created and needs to be persisted
     */
    public boolean isPersisted()                { return id != NEW_ID; }

    /**
     * Checks if two DTOs are equal.
     * <p>
     * DTOs are equal if:
     * <ul>
     *   <li>they are of the same type</li>
     *   <li>both are persisted and have the same ID</li>
     *   <li>both are not persisted and are the very same object</li>
     * </ul>
     * <p>
     * This equals method is safe for use in collections and maps.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (! this.getClass().isAssignableFrom(obj.getClass())) return false;

        assert obj instanceof BaseDto;

        BaseDto cmp = (BaseDto) obj;
        if (id == cmp.id){
            if (isPersisted()) {
                return true;
            } else {
                return super.equals(cmp);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (isPersisted()) {
            return (int) ((id>>32) ^ id);
        } else {
            return super.hashCode();
        }
    }

}
