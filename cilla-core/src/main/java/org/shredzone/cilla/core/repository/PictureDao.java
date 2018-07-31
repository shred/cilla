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

import org.shredzone.cilla.core.model.Picture;

/**
 * DAO for {@link Picture} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface PictureDao extends BaseDao<Picture> {

    /**
     * Fetches a {@link Picture} by its hash ID. If the picture was not found,
     * {@code null} is returned.
     *
     * @param hashId
     *            Picture hash ID
     * @return Picture or {@code null}
     */
    Picture fetchByHashId(String hashId);

}
