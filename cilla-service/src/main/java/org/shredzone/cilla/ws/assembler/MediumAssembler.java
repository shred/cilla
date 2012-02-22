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

import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.MediumDto;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link MediumDto} and {@link Medium}.
 * <p>
 * Projections are <em>not</em> supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class MediumAssembler extends AbstractAssembler<Medium, MediumDto> {

    private @Resource UserDao userDao;

    @Override
    public MediumDto assemble(Medium entity) throws CillaServiceException {
        MediumDto dto = new MediumDto();
        dto.setId(entity.getId());
        dto.setName(entity.getImage().getName());

        User creator = entity.getCreatedBy();
        dto.setCreatorId(creator.getId());
        dto.setCreatorLogin(creator.getLogin());
        dto.setCreatorName(creator.getName());

        return dto;
    }

    @Override
    public void merge(MediumDto dto, Medium entity) throws CillaServiceException {
        super.merge(dto, entity);
        entity.getImage().setName(dto.getName());
        entity.setCreatedBy(userDao.fetch(dto.getCreatorId()));
    }

}
