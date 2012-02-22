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
 * A generic service for online maps. The implementation decides which map service
 * provider is actually used.
 *
 * @author Richard "Shred" Körber
 */
public interface MapService {

    /**
     * Gets a JavaScript code for initialization.
     */
    String getInitJs();

    /**
     * Builds a HTML fragment for rendering the given {@link MapModel}.
     *
     * @param divId
     *            id of the div container the map is rendered in
     * @param data
     *            {@link MapModel} with all details needed for rendering
     * @return HTML fragment for rendering the map
     */
    String build(String divId, MapModel data);

}
