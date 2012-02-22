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

/**
 * Desired order of pages in a page result set.
 *
 * @author Richard "Shred" Körber
 */
public enum PageOrder {

    /**
     * Pages are sorted in the order they were created.
     * <p>
     * Advantage: Pages will never change their position within the page listing.
     * <p>
     * Disadvantage: Even updates won't change their position, so updates may be missed.
     */
    CREATION("creation"),

    /**
     * Pages are sorted by modification date.
     * <p>
     * Advantage: Most recently created or changed pages always appear at the top.
     * <p>
     * Disadvantage: Even minor changes (like correcting a typo) moves a page to the top
     * of the page listing.
     */
    MODIFICATION("modification"),

    /**
     * Pages are sorted by publication date.
     * <p>
     * Advantage: By adjusting the publishing date, the author has full control whether
     * the page should be moved to the front of the page listing (e.g. content update) or
     * not (e.g. typo fix).
     * <p>
     * Disadvantage: Author must remember to modify the publishing date. Original
     * publishing date is lost.
     */
    PUBLICATION("publication");

    private final String column;

    private PageOrder(String column) {
        this.column = column;
    }

    /**
     * The name of the property the pages are ordered by.
     *
     * @return Property name
     */
    public String getColumn() {
        return column;
    }

}
