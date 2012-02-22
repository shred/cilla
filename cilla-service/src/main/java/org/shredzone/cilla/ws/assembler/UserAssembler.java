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
import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.LanguageDao;
import org.shredzone.cilla.core.repository.RoleDao;
import org.shredzone.cilla.service.UserService;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.user.UserDto;
import org.springframework.stereotype.Component;

/**
 * {@link Assembler} for {@link UserDto} and {@link User}.
 * <p>
 * Projections are supported.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class UserAssembler extends AbstractAssembler<User, UserDto> {

    private @Resource LanguageDao languageDao;
    private @Resource RoleDao roleDao;
    private @Resource UserService userService;

    @Override
    public UserDto assemble(User entity) throws CillaServiceException {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setName(entity.getName());
        dto.setMail(entity.getMail());
        dto.setRoleName(entity.getRole().getName());
        dto.setLanguageId(entity.getLanguage().getId());
        dto.setLanguage(entity.getLanguage().getLocale());
        dto.setTimeZone(entity.getTimeZone());
        return dto;
    }

    @Override
    public void merge(UserDto dto, User entity) throws CillaServiceException {
        super.merge(dto, entity);
        entity.setName(dto.getName());
        entity.setMail(dto.getMail());
        entity.setTimeZone(dto.getTimeZone());

        if (!(entity.getLogin().equals(dto.getLogin()))) {
            userService.changeLogin(entity, dto.getLogin());
        }

        Role role = roleDao.fetchByName(dto.getRoleName());
        if (!(entity.getRole().equals(role))) {
            userService.changeRole(entity, role);
        }

        entity.setLanguage(languageDao.fetch(dto.getLanguageId()));
    }

    @Override
    public ProjectionList projection() {
        ProjectionList projection = Projections.projectionList();
        projection.add(Projections.id(), "id");
        projection.add(Property.forName("login")       .as("login"));
        projection.add(Property.forName("name")        .as("name"));
        projection.add(Property.forName("mail")        .as("mail"));
        projection.add(Property.forName("timeZone")    .as("timeZone"));
        projection.add(Property.forName("r.name")      .as("roleName"));
        projection.add(Property.forName("l.id")        .as("languageId"));
        projection.add(Property.forName("l.locale")    .as("language"));
        return projection;
    }

}
