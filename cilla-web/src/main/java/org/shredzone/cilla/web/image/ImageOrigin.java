/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2017 Richard "Shred" Körber
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
package org.shredzone.cilla.web.image;

import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Picture;

/**
 * Describes the image origin.
 *
 * @author Richard "Shred" Körber
 */
public enum ImageOrigin {

    /**
     * A {@link Picture} image.
     */
    PICTURE,

    /**
     * A {@link Header} image ({@link Header#getHeaderImage()}).
     */
    HEADER,

    /**
     * A {@link Header} image ({@link Header#getFullImage()}).
     */
    HEADER_FULL,

    /**
     * A {@link Medium} image.
     */
    MEDIUM;

}
