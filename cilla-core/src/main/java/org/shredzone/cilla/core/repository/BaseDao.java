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
package org.shredzone.cilla.core.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.shredzone.cilla.core.model.BaseModel;

/**
 * Base class of all DAOs.
 *
 * @author Richard "Shred" Körber
 */
public interface BaseDao<T extends BaseModel> {

    /**
     * Persists a new entity.
     *
     * @param data
     *            Entity to persist.
     */
    void persist(T data);

    /**
     * Merges a detached entity.
     *
     * @param data
     *            Entity to merge.
     * @return Merged entity.
     */
    T merge(T data);

    /**
     * Fetches the entity with the given ID.
     *
     * @param id
     *            Entity ID
     * @return Entity, or {@code null} if there was no such entity.
     */
    T fetch(long id);

    /**
     * Deletes an entity.
     *
     * @param data
     *            Entity to be deleted.
     */
    void delete(T data);

    /**
     * Count all entities.
     *
     * @return Number of entities in the database
     */
    long countAll();

    /**
     * Fetches all entities.
     *
     * @return List of all entities
     */
    List<T> fetchAll();

    /**
     * Creates a {@link Critera} object that will result entities of this type.
     * <p>
     * The {@link Criteria} is bound to the current session.
     *
     * @return {@link Criteria} that was generated
     */
    Criteria criteria();

}
