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

import javax.annotation.Resource;

import org.shredzone.cilla.core.event.Event;
import org.shredzone.cilla.core.event.EventService;
import org.shredzone.cilla.core.event.EventType;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.repository.SectionDao;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.SectionDto;

/**
 * {@link AbstractAssembler} for sections, implementing the {@link SectionAssembler}.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractSectionAssembler<F extends Section, T extends SectionDto>
        extends AbstractAssembler<F, T>
        implements SectionAssembler<F, T> {

    private @Resource EventService eventService;
    private @Resource SectionDao sectionDao;

    /**
     * Adds a {@link Section} to a {@link Page}.
     *
     * @param page
     *            {@link Page} to add the section to
     * @param section
     *            {@link Section} to be added
     */
    protected void addSection(Page page, F section) throws CillaServiceException {
        section.setPage(page);
        sectionDao.persist(section);
        page.getSections().add(section);
        renumberSectionSequence(section.getPage());
        eventService.fireEvent(new Event<Section>(EventType.SECTION_NEW, section));
    }

    /**
     * Updates a {@link Section}.
     *
     * @param section
     *            {@link Section} to update
     */
    protected void updateSection(F section) throws CillaServiceException {
        renumberSectionSequence(section.getPage());
        eventService.fireEvent(new Event<Section>(EventType.SECTION_UPDATE, section));
    }

    /**
     * Removes a {@link Section} with all its dependencies.
     *
     * @param section
     *            {@link Section} to be deleted
     */
    protected void removeSection(F section) throws CillaServiceException {
        section.getPage().getSections().remove(section);
        sectionDao.delete(section);
        renumberSectionSequence(section.getPage());
        eventService.fireEvent(new Event<Section>(EventType.SECTION_DELETE, section));
    }

    /**
     * Renumbers the sequence of the sections in a {@link Page}.
     *
     * @param page
     *            {@link Page} for renumbering
     */
    private void renumberSectionSequence(Page page) {
        int sequence = 0;
        for (Section sec : page.getSections()) {
            sec.setSequence(sequence++);
        }
    }

}
