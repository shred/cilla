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

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.model.embed.Geolocation;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.header.HeaderDto;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link HeaderDto} and {@link Header}.
 * <p>
 * Projections are supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class HeaderAssembler extends AbstractAssembler<Header, HeaderDto> {

    private @Resource UserDao userDao;

    @Override
    public HeaderDto assemble(Header entity) throws CillaServiceException {
        HeaderDto dto = new HeaderDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCaption(entity.getCaption());
        dto.setCreation(entity.getCreation());
        dto.setCommentable(entity.getThread().isCommentable());
        dto.setEnabled(entity.isEnabled());

        if (entity.getDescription() != null) {
            dto.setDescription(entity.getDescription().getText());
            dto.setDescriptionFormat(entity.getDescription().getFormat());
        }

        if (entity.getCreator() != null) {
            dto.setCreatorId(entity.getCreator().getId());
            dto.setCreatorLogin(entity.getCreator().getLogin());
            dto.setCreatorName(entity.getCreator().getName());
        }

        Geolocation gl = entity.getLocation();
        if (gl != null) {
            dto.setLongitude(gl.getLongitude());
            dto.setLatitude(gl.getLatitude());
            dto.setAltitude(gl.getAltitude());
        }

        return dto;
    }

    @Override
    public void merge(HeaderDto dto, Header header) throws CillaServiceException {
        super.merge(dto, header);

        header.setName(dto.getName());
        header.setCaption(dto.getCaption());
        header.setCreator(userDao.fetch(dto.getCreatorId()));
        header.setCreation(dto.getCreation());
        header.setEnabled(dto.isEnabled());

        header.getThread().setCommentable(dto.isCommentable());

        if (dto.getLongitude() != null && dto.getLatitude() != null) {
            Geolocation gl = new Geolocation();
            gl.setLongitude(dto.getLongitude());
            gl.setLatitude(dto.getLatitude());
            gl.setAltitude(dto.getAltitude());
            header.setLocation(gl);
        } else {
            header.setLocation(null);
        }

        if (dto.getDescription() != null && dto.getDescriptionFormat() != null) {
            header.setDescription(new FormattedText(dto.getDescription(), dto.getDescriptionFormat()));
        } else {
            header.setDescription(null);
        }
    }

    @Override
    public ProjectionList projection() {
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.id(), "id");
        projection.add(Property.forName("name")        .as("name"));
        projection.add(Property.forName("caption")     .as("caption"));
        projection.add(Property.forName("description.text").as("description"));
        projection.add(Property.forName("description.format").as("descriptionFormat"));
        projection.add(Property.forName("creation")    .as("creation"));
        projection.add(Property.forName("enabled")     .as("enabled"));
        projection.add(Property.forName("location.longitude").as("longitude"));
        projection.add(Property.forName("location.latitude").as("latitude"));
        projection.add(Property.forName("location.altitude").as("altitude"));
        projection.add(Property.forName("t.commentable").as("commentable"));
        projection.add(Property.forName("c.id")        .as("creatorId"));
        projection.add(Property.forName("c.login")     .as("creatorLogin"));
        projection.add(Property.forName("c.name")      .as("creatorName"));
        return projection;
    }

}
