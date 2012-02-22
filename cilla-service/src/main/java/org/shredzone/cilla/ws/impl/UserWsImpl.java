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
package org.shredzone.cilla.ws.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.hibernate.Criteria;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.service.UserService;
import org.shredzone.cilla.ws.AbstractWs;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.assembler.UserAssembler;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.user.UserDto;
import org.shredzone.cilla.ws.user.UserWs;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link UserWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "UserWs",
    endpointInterface = "org.shredzone.cilla.ws.user.UserWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class UserWsImpl extends AbstractWs implements UserWs {

    private @Resource UserAssembler userAssembler;
    private @Resource UserService userService;
    private @Resource UserDao userDao;

    @Override
    public UserDto fetchByLogin(String login) throws CillaServiceException {
        User user = userDao.fetchByLogin(login);
        return (user != null ? userAssembler.assemble(user) : null);
    }

    @Override
    public UserDto fetch(long id) throws CillaServiceException {
        return userAssembler.assemble(userDao.fetch(id));
    }

    @Override
    public long count() {
        return userDao.countAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserDto> list(ListRange lr) {
        Criteria crit = userDao.criteria()
            .createAlias("role", "r")
            .createAlias("language", "l")
            .setProjection(userAssembler.projection())
            .setResultTransformer(new AliasToBeanResultTransformer(UserDto.class));

        applyListRange(lr, "login", false, crit);

        return crit.list();
    }

    @Override
    public UserDto createNew() throws CillaServiceException {
        return userAssembler.assemble(userService.createNew());
    }

}
