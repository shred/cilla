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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.shredzone.cilla.core.model.Language;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.system.LanguageDto;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link LanguageDto} and {@link Language}.
 * <p>
 * Projections are supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class LanguageAssembler extends AbstractAssembler<Language, LanguageDto> {

    @Override
    public LanguageDto assemble(Language entity) throws CillaServiceException {
        LanguageDto dto = new LanguageDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setLocale(entity.getLocale());
        return dto;
    }

    @Override
    public void merge(LanguageDto dto, Language entity) throws CillaServiceException {
        super.merge(dto, entity);
        entity.setName(dto.getName());
        entity.setLocale(dto.getLocale());
    }

    @Override
    public ProjectionList projection() {
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.id(), "id");
        projection.add(Property.forName("name")        .as("name"));
        projection.add(Property.forName("locale")      .as("locale"));
        return projection;
    }

}
