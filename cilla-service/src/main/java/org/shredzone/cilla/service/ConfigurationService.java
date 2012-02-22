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
package org.shredzone.cilla.service;

/**
 * A service for global configuration settings.
 *
 * @author Richard "Shred" Körber
 */
public interface ConfigurationService {

    /**
     * Gets a configuration option.
     *
     * @param <T>
     *            the option type
     * @param key
     *            the option key
     * @param def
     *            a default value, may be {@code null}
     * @return the configuration value, or default value if it was unset
     */
    <T> T get(String key, T def);

    /**
     * Puts a configuration option
     *
     * @param <T>
     *            the option type
     * @param key
     *            the option key
     * @param value
     *            the option value
     */
    <T> void put(String key, T value);

    /**
     * Removes a configuration option.
     *
     * @param key
     *            the option key
     */
    void remove(String key);

    /**
     * Checks if an option key is set.
     *
     * @param key
     *            the option key
     * @return {@code true} if the key was set
     */
    boolean hasKey(String key);

}
