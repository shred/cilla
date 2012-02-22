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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.shredzone.cilla.core.model.Authority;
import org.shredzone.cilla.core.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * An extension of {@link UserDetails} that also provides the database ID, user name and
 * role name of the user that is currently logged in.
 * <p>
 * This object is immutable.
 *
 * @author Richard "Shred" Körber
 */
public class CillaUserDetails implements UserDetails {
    private static final long serialVersionUID = 2236646416270618056L;

    private final long userId;
    private final String login;
    private final String name;
    private final String encryptedPassword;
    private final String role;
    private final Collection<GrantedAuthority> rights;

    /**
     * Instantiates a new {@link CillaUserDetails} for the given {@link User}.
     *
     * @param user
     *            the {@link User}
     */
    public CillaUserDetails(User user) {
        userId = user.getId();
        login = user.getLogin();
        name = user.getName();
        encryptedPassword = user.getPassword();

        role = user.getRole().getName();

        Collection<Authority> roleauhtorities = user.getRole().getAuthorities();
        GrantedAuthority[] ga = new GrantedAuthority[roleauhtorities.size()];
        int ix = 0;
        for (Authority authority : roleauhtorities) {
            ga[ix++] = new SimpleGrantedAuthority("ROLE_" + authority.getName());
        }
        rights = Collections.unmodifiableCollection(Arrays.asList(ga));
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() { return rights; }

    @Override
    public String getPassword()                 { return encryptedPassword; }

    @Override
    public String getUsername()                 { return login; }

    @Override
    public boolean isAccountNonExpired()        { return true; }

    @Override
    public boolean isAccountNonLocked()         { return true; }

    @Override
    public boolean isCredentialsNonExpired()    { return true; }

    @Override
    public boolean isEnabled()                  { return true; }

    /**
     * User's ID.
     */
    public long getUserId()                     { return userId; }

    /**
     * User's full name.
     */
    public String getName()                     { return name; }

    /**
     * User's role name.
     */
    public String getRole()                     { return role; }

}
