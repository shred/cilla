/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2018 Richard "Shred" Körber
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

import org.shredzone.cilla.core.model.EmbedSection;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.SectionDao;
import org.shredzone.cilla.ws.assembler.annotation.SectionAssemblerType;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.EmbedSectionDto;
import org.springframework.stereotype.Component;

/**
 * {@link SectionAssembler} for {@link EmbedSectionDto} and {@link EmbedSection}.
 * <p>
 * Projections are <em>not</em> supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
@SectionAssemblerType(type = "embed", entity = EmbedSection.class, dto = EmbedSectionDto.class)
public class EmbedSectionAssembler extends AbstractSectionAssembler<EmbedSection, EmbedSectionDto> {

    private @Resource SectionDao sectionDao;

    @Override
    public EmbedSectionDto assemble(EmbedSection entity) {
        EmbedSectionDto dto = new EmbedSectionDto();
        dto.setId(entity.getId());
        dto.setEmbedUrl(entity.getEmbedUrl());
        return dto;
    }

    @Override
    public void merge(EmbedSectionDto dto, EmbedSection entity) {
        entity.setEmbedUrl(dto.getEmbedUrl());
    }

    @Override
    public EmbedSection persistSection(EmbedSectionDto dto, Page page) throws CillaServiceException {
        EmbedSection sec;

        if (dto.isPersisted()) {
            sec = (EmbedSection) sectionDao.fetch(dto.getId());
            merge(dto, sec);
            updateSection(sec);

        } else {
            sec = new EmbedSection();
            merge(dto, sec);
            addSection(page, sec);
        }

        return sec;
    }

    @Override
    public EmbedSectionDto createSection() throws CillaServiceException {
        return assemble(new EmbedSection());
    }

    @Override
    public void delete(EmbedSection entity) throws CillaServiceException {
        removeSection(entity);
    }

}
