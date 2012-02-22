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
package org.shredzone.cilla.web.page;

import javax.servlet.http.HttpSession;

import org.shredzone.cilla.core.model.BaseModel;

/**
 * A manager for unlocking protected entities. This may be password-protected pages,
 * gallery pictures protected against deep-linking, etc...
 * <p>
 * Unlocking information is stored in the session. The entity is unlocked as long as the
 * session lives.
 * <p>
 * Note that this locking mechanism is prone to session stealing. Do not protect sensitive
 * data (like administration pages) with this service!
 *
 * @author Richard "Shred" Körber
 */
public interface ResourceLockManager {

    /**
     * Unlocks an entity for the given session.
     *
     * @param session
     *            {@link HttpSession} that keeps the unlocking information
     * @param entity
     *            entity to be unlocked
     */
    void unlockStore(HttpSession session, BaseModel entity);

    /**
     * Checks if an entity is unlocked in the given session.
     *
     * @param session
     *            {@link HttpSession} that keeps the unlocking information
     * @param entity
     *            entity to be checked
     * @return {@code true} if the entity is unlocked
     */
    boolean isUnlocked(HttpSession session, BaseModel entity);

}
