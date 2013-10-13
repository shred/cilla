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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.ProjectionList;
import org.shredzone.cilla.core.model.BaseModel;
import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Abstract implementation of {@link Assembler}. It offers some basic implementations.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractAssembler<F extends BaseModel, T extends BaseDto> implements Assembler<F, T> {

    /**
     * Assembles a collection of entities with this assembler.
     *
     * @param from
     *            {@link Collection} to assemble
     * @return List containing the assembled DTO
     */
    public List<T> bulkAssemble(Collection<F> from) throws CillaServiceException {
        List<T> result = new ArrayList<T>(from.size());
        for (F entity : from) {
            result.add(assemble(entity));
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the DTO was created from a persisted entity, this implementation makes sure that
     * it is merged with that entity again.
     *
     * @throws IllegalArgumentException
     *             if the DTO is about to be merged to a different entity
     */
    @Override
    public void merge(T dto, F entity) throws CillaServiceException {
        if (dto.isPersisted() && entity.getId() != dto.getId()) {
            throw new IllegalArgumentException("cannot merge, different ids: dto "
                            + dto.getId() + " != entity " + entity.getId());
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation throws an {@link UnsupportedOperationException}.
     */
    @Override
    public ProjectionList projection() {
        throw new UnsupportedOperationException("projections not supported");
    }

}
