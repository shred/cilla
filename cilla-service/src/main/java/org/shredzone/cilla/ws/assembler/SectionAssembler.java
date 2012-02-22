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

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.SectionDto;

/**
 * Extended {@link Assembler} for {@link SectionDto} and {@link Section}.
 *
 * @author Richard "Shred" Körber
 */
public interface SectionAssembler<F extends Section, T extends SectionDto> extends Assembler<F, T> {

    /**
     * Creates a new, empty section of this type.
     */
    T createSection() throws CillaServiceException;

    /**
     * Persists a section.
     *
     * @param dto
     *            {@link SectionDto} to be persisted
     * @param page
     *            {@link Page} to persist the section to
     * @return persisted {@link Section}
     */
    F persistSection(T dto, Page page) throws CillaServiceException;

    /**
     * Deletes a section.
     *
     * @param entity {@link Section} to delete
     */
    void delete(F entity) throws CillaServiceException;

}
