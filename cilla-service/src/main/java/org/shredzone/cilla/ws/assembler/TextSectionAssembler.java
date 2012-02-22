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

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.TextSection;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.repository.SectionDao;
import org.shredzone.cilla.ws.assembler.annotation.SectionAssemblerType;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.TextSectionDto;
import org.springframework.stereotype.Component;

/**
 * {@link SectionAssembler} for {@link TextSectionDto} and {@link TextSection}.
 * <p>
 * Projections are <em>not</em> supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
@SectionAssemblerType(type = "text", entity = TextSection.class, dto = TextSectionDto.class)
public class TextSectionAssembler extends AbstractSectionAssembler<TextSection, TextSectionDto> {

    private @Resource SectionDao sectionDao;

    @Override
    public TextSectionDto assemble(TextSection entity) {
        TextSectionDto dto = new TextSectionDto();
        dto.setId(entity.getId());

        if (entity.getText() != null) {
            dto.setText(entity.getText().getText());
            dto.setTextFormat(entity.getText().getFormat());
        }

        return dto;
    }

    @Override
    public void merge(TextSectionDto dto, TextSection entity) {
        if (dto.getText() != null && dto.getTextFormat() != null) {
            entity.setText(new FormattedText(dto.getText(), dto.getTextFormat()));
        } else {
            entity.setText(null);
        }
    };

    @Override
    public TextSection persistSection(TextSectionDto dto, Page page) throws CillaServiceException {
        TextSection sec;

        if (dto.isPersisted()) {
            sec = (TextSection) sectionDao.fetch(dto.getId());
            merge(dto, sec);
            updateSection(sec);

        } else {
            sec = new TextSection();
            merge(dto, sec);
            addSection(page, sec);
        }

        return sec;
    }

    @Override
    public TextSectionDto createSection() throws CillaServiceException {
        return assemble(new TextSection());
    }

    @Override
    public void delete(TextSection entity) throws CillaServiceException {
        removeSection(entity);
    }

}
