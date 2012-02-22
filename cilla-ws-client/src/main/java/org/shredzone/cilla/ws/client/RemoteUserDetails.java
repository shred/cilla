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
package org.shredzone.cilla.ws.client;

import java.util.Collection;

import org.shredzone.cilla.ws.user.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * {@link UserDetails} of the user who is remotely logged in.
 *
 * @author Richard "Shred" Körber
 */
public class RemoteUserDetails implements UserDetails {
    private static final long serialVersionUID = 4581452789593137366L;

    private String username;
    private String password;
    private UserDto user;
    private Collection<GrantedAuthority> authorities;

    @Override
    public String getUsername()                 { return username; }
    public void setUsername(String username)    { this.username = username; }

    @Override
    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public void setAuthorities(Collection<GrantedAuthority> authorities) { this.authorities = authorities; }

    public UserDto getUser()                    { return user; }
    public void setUser(UserDto user)           { this.user = user; }

    @Override
    public boolean isAccountNonExpired()        { return true; }

    @Override
    public boolean isAccountNonLocked()         { return true; }

    @Override
    public boolean isCredentialsNonExpired()    { return true; }

    @Override
    public boolean isEnabled()                  { return true; }

}
