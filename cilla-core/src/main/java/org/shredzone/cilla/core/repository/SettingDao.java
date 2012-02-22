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

import org.shredzone.cilla.core.model.Setting;

/**
 * DAO for {@link Setting} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface SettingDao extends BaseDao<Setting> {

    /**
     * Fetches a setting by its key.
     *
     * @param key
     *            Setting key
     * @return Setting, or {@code null} if there was no such key
     */
    Setting fetchByKey(String key);

    /**
     * Creates a new setting entry with the given key and value. If there is already a
     * setting with that key, its value is updated.
     *
     * @param key
     *            Setting key
     * @param value
     *            New value of that setting, or {@code null} to remove it
     * @return Setting that was created or updated, or {@code null} if the setting was
     *         deleted
     */
    Setting createOrUpdate(String key, String value);

}
