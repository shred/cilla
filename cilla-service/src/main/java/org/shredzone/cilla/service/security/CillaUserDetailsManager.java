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
package org.shredzone.cilla.service.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Authority;
import org.shredzone.cilla.core.model.Role;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.RoleDao;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.service.UserService;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * An {@link UserDetailsManager} implementation that uses Cilla's user database for
 * authentication.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class CillaUserDetailsManager implements UserDetailsManager, GroupManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource UserService userService;
    private @Resource UserDao userDao;
    private @Resource RoleDao roleDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        User user = userDao.fetchByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        log.info("Successfully logged in: {}", user.getLogin());

        return new CillaUserDetails(user);
    }

    @Override
    public void createUser(UserDetails user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void updateUser(UserDetails user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteUser(String username) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        if (currentUser == null) {
            throw new AccessDeniedException("No user is logged in");
        }

        String username = currentUser.getName();
        User user = userDao.fetchByLogin(username);
        if (user == null) {
            throw new AccessDeniedException("User without account");
        }

        try {
            userService.changePassword(user, oldPassword, newPassword);
        } catch (CillaServiceException ex) {
            throw new AccessDeniedException("Could not change password", ex);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userDao.fetchByLogin(username) != null;
    }

    @Override
    public List<String> findAllGroups() {
        List<String> result = new ArrayList<String>();
        for (Role role : roleDao.fetchAll()) {
            result.add(role.getName());
        }
        return result;
    }

    @Override
    public List<String> findUsersInGroup(String groupName) {
        List<String> result = new ArrayList<String>();
        Role role = roleDao.fetchByName(groupName);
        if (role != null) {
            for (User user : userDao.fetchAllWithRole(role)) {
                result.add(user.getLogin());
            }
        }
        return result;
    }

    @Override
    public void createGroup(String groupName, List<GrantedAuthority> authorities) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteGroup(String groupName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void renameGroup(String oldName, String newName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addUserToGroup(String username, String group) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeUserFromGroup(String username, String groupName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<GrantedAuthority> findGroupAuthorities(String groupName) {
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        Role role = roleDao.fetchByName(groupName);
        if (role != null) {
            for (Authority auth : role.getAuthorities()) {
                result.add(new SimpleGrantedAuthority("ROLE_" + auth.getName()));
            }
        }
        return result;
    }

    @Override
    public void addGroupAuthority(String groupName, GrantedAuthority authority) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeGroupAuthority(String groupName, GrantedAuthority authority) {
        throw new UnsupportedOperationException();
    }

}
