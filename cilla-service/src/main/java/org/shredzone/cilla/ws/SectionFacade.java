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

import java.util.Collection;
import java.util.List;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.ws.assembler.SectionAssembler;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.SectionDto;

/**
 * A facade for invoking {@link SectionAssembler}. The facade detects the section type,
 * and delegates the request to the respective {@link SectionAssembler} implementation.
 *
 * @author Richard "Shred" Körber
 */
public interface SectionFacade {

    /**
     * Assemble all sections.
     *
     * @param sections
     *            List of {@link Section} to assemble
     * @return List of assembled {@link SectionDto}.
     */
    List<SectionDto> assembleSections(List<Section> sections) throws CillaServiceException;

    /**
     * Persists a single {@link SectionDto}.
     *
     * @param dto
     *            {@link SectionDto} to persist
     * @param page
     *            {@link Page} this section belongs to
     * @return {@link Section} that was persisted
     */
    Section persistSection(SectionDto dto, Page page) throws CillaServiceException;

    /**
     * Deletes a {@link Section} and all its dependencies.
     *
     * @param entity
     *            {@link Section} to delete
     */
    void deleteSection(Section entity) throws CillaServiceException;

    /**
     * Creates a new {@link SectionDto} of the given type.
     *
     * @param type
     *            Section type
     * @return {@link SectionDto} that was created
     */
    SectionDto createSection(String type) throws CillaServiceException;

    /**
     * Gets a collection of all defined section types.
     */
    Collection<String> getSectionTypes();

}
