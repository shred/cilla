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
package org.shredzone.cilla.core.event;

/**
 * Enumeration of all available event types.
 *
 * @author Richard "Shred" Körber
 */
public enum EventType {

    PAGE_NEW, PAGE_UPDATE, PAGE_PUBLISH, PAGE_UNPUBLISH, PAGE_DELETE,

    SECTION_NEW, SECTION_UPDATE, SECTION_DELETE,

    GALLERY_PICTURE_NEW, GALLERY_PICTURE_UPDATE, GALLERY_PICTURE_DELETE,

    HEADER_NEW, HEADER_UPDATE, HEADER_DELETE,

    CUSTOM;

}
