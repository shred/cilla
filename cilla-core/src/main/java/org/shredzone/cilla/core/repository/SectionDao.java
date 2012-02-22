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

import org.hibernate.Criteria;
import org.shredzone.cilla.core.model.Section;

/**
 * DAO for {@link Section} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface SectionDao extends BaseDao<Section> {

    /**
     * Creates a {@link Critera} object that will result entities of the given type. This
     * method is used to return only entities of the given Section subtype.
     * <p>
     * The {@link Criteria} is bound to the current session.
     *
     * @param type
     *            Section type to return
     * @return {@link Criteria} that was generated
     */
    Criteria criteria(Class<? extends Section> type);

    /**
     * Fetches a Section with the given type.
     *
     * @param <T>
     *            Section type
     * @param id
     *            Section id
     * @param clazz
     *            Class that is expected as result
     * @return {@link Section} found, or {@code null} if there was no such section, or the
     *         section was not of the desired type.
     */
    <T extends Section> T fetchAs(long id, Class<T> clazz);

}
