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
package org.shredzone.cilla.web.map;

/**
 * Enumeration of available map types.
 * <p>
 * Note that some {@link MapService} implementations may not support all types. If a type
 * is not supported by the implementation, it should fall back to a supported type that is
 * as close as possible to the desired result.
 *
 * @author Richard "Shred" Körber
 */
public enum MapType {

    /**
     * Plain roadmap. Should be supported by all {@link MapService} implementation.
     */
    ROADMAP,

    /**
     * Satellite view.
     */
    SATELLITE;

}
