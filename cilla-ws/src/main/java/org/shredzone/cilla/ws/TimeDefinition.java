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
 * Available time definitions for dating pictures.
 *
 * @author Richard "Shred" Körber
 */
public enum TimeDefinition {

    /*
     * IMPORTANT NOTE: ALWAYS APPEND NEW ENTRIES TO THE END! Never change the sequence,
     * delete entries, or insert entries, since the TimeDefinition choice is persisted by
     * its index, not its name.
     */

    /**
     * No date is to be rendered at all.
     */
    NONE,

    /**
     * The entire date and time is usable.
     */
    FULL,

    /**
     * Only the date is accurate. Time must be ignored.
     */
    DATE,

    /**
     * The week of year and the year is usable.
     */
    WEEK,

    /**
     * Only the month and the year is usable. Day and time must be ignored.
     */
    MONTH,

    /**
     * Only the year is usable.
     */
    YEAR;

}
