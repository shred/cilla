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

import java.util.SortedSet;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.model.embed.Geolocation;
import org.shredzone.cilla.core.repository.TagDao;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.PictureDto;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link PictureDto} and {@link Picture}.
 * <p>
 * Projections are supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class PictureAssembler extends AbstractAssembler<Picture, PictureDto> {

    private @Resource TagDao tagDao;

    @Override
    public PictureDto assemble(Picture entity) throws CillaServiceException {
        PictureDto dto = new PictureDto();
        dto.setId(entity.getId());
        dto.setCreateDate(entity.getCreateDate());
        dto.setCreateTimeZone(entity.getCreateTimeZone());
        dto.setCreateTimeDefinition(entity.getCreateTimeDefinition());
        dto.setCommentable(entity.getThread().isCommentable());

        if (entity.getCaption() != null) {
            dto.setCaption(entity.getCaption().getText());
            dto.setCaptionFormat(entity.getCaption().getFormat());
        }

        Geolocation gl = entity.getLocation();
        if (gl != null) {
            dto.setLongitude(gl.getLongitude());
            dto.setLatitude(gl.getLatitude());
            dto.setAltitude(gl.getAltitude());
        }

        for (Tag tag : entity.getTags()) {
            dto.getTags().add(tag.getName());
        }

        return dto;
    }

    @Override
    public void merge(PictureDto dto, Picture entity) throws CillaServiceException {
        super.merge(dto, entity);
        entity.setCreateDate(dto.getCreateDate());

        entity.setCreateTimeZone(dto.getCreateTimeZone());
        entity.setCreateTimeDefinition(dto.getCreateTimeDefinition());
        entity.getThread().setCommentable(dto.isCommentable());

        if (dto.getCaption() != null && dto.getCaptionFormat() != null) {
            entity.setCaption(new FormattedText(dto.getCaption(), dto.getCaptionFormat()));
        } else {
            entity.setCaption(null);
        }

        if (dto.getLongitude() != null && dto.getLatitude() != null) {
            Geolocation gl = new Geolocation();
            gl.setLongitude(dto.getLongitude());
            gl.setLatitude(dto.getLatitude());
            gl.setAltitude(dto.getAltitude());
            entity.setLocation(gl);
        } else {
            entity.setLocation(null);
        }

        SortedSet<Tag> tagSet = entity.getTags();
        tagSet.clear();
        for (String tag : dto.getTags()) {
            tagSet.add(tagDao.fetchOrCreate(tag));
        }
    }

}
