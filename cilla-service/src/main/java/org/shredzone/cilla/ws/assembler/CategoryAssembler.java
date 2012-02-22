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
import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.ws.category.CategoryDto;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link CategoryDto} and {@link Category}.
 * <p>
 * Projections are supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class CategoryAssembler extends AbstractAssembler<Category, CategoryDto> {

    @Override
    public CategoryDto assemble(Category entity) throws CillaServiceException {
        CategoryDto dto = new CategoryDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setTitle(entity.getTitle());
        dto.setIcon(entity.getIcon());
        if (entity.getCaption() != null) {
            dto.setCaption(entity.getCaption().getText());
            dto.setCaptionFormat(entity.getCaption().getFormat());
        }
        return dto;
    }

    @Override
    public void merge(CategoryDto dto, Category entity) throws CillaServiceException {
        super.merge(dto, entity);

        entity.setName(dto.getName());
        entity.setTitle(dto.getTitle());
        entity.setIcon(dto.getIcon());

        if (dto.getCaption() != null && dto.getCaptionFormat() != null) {
            entity.setCaption(new FormattedText(dto.getCaption(), dto.getCaptionFormat()));
        } else {
            entity.setCaption(null);
        }
    }

    @Override
    public ProjectionList projection() {
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.id(), "id");
        projection.add(Property.forName("name")        .as("name"));
        projection.add(Property.forName("title")       .as("title"));
        projection.add(Property.forName("icon")        .as("icon"));
        projection.add(Property.forName("caption.text").as("caption"));
        projection.add(Property.forName("caption.format").as("captionFormat"));
        return projection;
    }

}
