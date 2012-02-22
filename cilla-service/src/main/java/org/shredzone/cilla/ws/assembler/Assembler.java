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
package org.shredzone.cilla.ws.assembler;

import org.hibernate.criterion.ProjectionList;
import org.shredzone.cilla.core.model.BaseModel;
import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * An assembler converts model entities to DTOs and vice versa. It also offers a
 * projection for bulk queries of the entity by criteria.
 *
 * @author Richard "Shred" Körber
 */
public interface Assembler<F extends BaseModel, T extends BaseDto> {

    /**
     * Assembles a DTO from the given entity.
     *
     * @param entity
     *            Entity to convert
     * @return DTO that was assembled
     */
    T assemble(F entity) throws CillaServiceException;

    /**
     * Writes back the DTO properties to the entity.
     *
     * @param dto
     *            DTO to merge
     * @param entity
     *            Entity to merge into
     */
    void merge(T dto, F entity) throws CillaServiceException;

    /**
     * Creates a {@link ProjectionList} for bulk queries of the DTO.
     *
     * @return {@link ProjectionList}
     * @throws UnsupportedOperationException
     *             if this assembler does not support projections
     */
    ProjectionList projection();

}
