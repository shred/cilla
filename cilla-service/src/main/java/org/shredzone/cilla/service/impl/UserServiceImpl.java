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
package org.shredzone.cilla.service.impl;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.service.SecurityService;
import org.shredzone.cilla.service.UserService;
import org.shredzone.cilla.service.security.CillaUserDetails;
import org.shredzone.cilla.ws.exception.CillaParameterException;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UserService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private @Resource UserDao userDao;
    private @Resource SecurityService securityService;

    @Override
    public User createNew() {
        return new User();
    }

    @Override
    public void updateUser(User user) throws CillaServiceException {
        if (user.getPassword() != null) {
            throw new CillaServiceException("Use changePassword for changing passwords");
        }

        User attached = userDao.fetch(user.getId());
        if (attached == null) {
            throw new CillaServiceException("User id " + user.getId() + " not found");
        }

        if (!attached.getLogin().equals(user.getLogin())) {
            throw new CillaServiceException("Login name cannot be changed!");
        }

        securityService.ifRole("ROLE_ROLEADMIN", role -> {
            attached.setRole(user.getRole());
        });

        attached.setName(user.getName());
        attached.setMail(user.getMail());
        attached.setLanguage(user.getLanguage());
    }

    @Override
    public void deleteUser(User user) {
        // Do not use merge, to avoid involuntary changes to the database entity
        User attached = userDao.fetch(user.getId());
        userDao.delete(attached);
    }

    @Override
    public void changeLogin(User user, String newLogin) throws CillaServiceException {
        if (newLogin == null || newLogin.isEmpty()) {
            throw new CillaParameterException("new login name must not be empty");
        }

        User check = userDao.fetchByLogin(newLogin);
        if (check != null && !check.equals(user)) {
            throw new CillaParameterException("login name '" + newLogin + "' is already used");
        }

        user.setLogin(newLogin);
    }

    @Override
    public void changePassword(User user, String oldPwd, String newPwd)
    throws CillaServiceException {
        if (user == null || oldPwd == null || newPwd == null) {
            throw new NullPointerException("null argument");
        }

        user.setPassword(newPwd);

        CillaUserDetails ud = new CillaUserDetails(user);
        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(user, user.getPassword(), ud.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);
    }

    @Override
    public void changeRole(User user, Role role) throws CillaServiceException {
        user.setRole(role);
    }

}
