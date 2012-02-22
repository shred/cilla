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
package org.shredzone.cilla.ws.exception;

/**
 * This exception is thrown when an entity was not found.
 *
 * @author Richard "Shred" Körber
 */
public class CillaNotFoundException extends CillaServiceException {
    private static final long serialVersionUID = -1862842776422778249L;

    private final String entity;
    private final long id;

    /**
     * Creates a new {@link CillaNotFoundException}.
     *
     * @param entity
     *            name of the entity that was not found
     * @param id
     *            ID of the entry that was not found
     */
    public CillaNotFoundException(String entity, long id) {
        super(entity + ": " + id);
        this.entity = entity;
        this.id = id;
    }

    /**
     * Returns the name of the entity.
     */
    public String getEntity() {
        return entity;
    }

    /**
     * Returns the id of the entry.
     */
    public long getId() {
        return id;
    }

}
