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
package org.shredzone.cilla.service.link;

import java.io.Serializable;

/**
 * Implementing classes can be passed to the {@link LinkBuilder} in order to transfer its
 * state to the link.
 *
 * @author Richard "Shred" Körber
 */
public interface Linkable extends Serializable {

    /**
     * Transfers the link target to the given {@link LinkBuilder}.
     *
     * @param builder
     *            {@link LinkBuilder} to transfer the link target to
     */
    void transfer(LinkBuilder builder);

}
