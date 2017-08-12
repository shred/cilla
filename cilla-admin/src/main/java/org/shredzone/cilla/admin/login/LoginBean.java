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
package org.shredzone.cilla.admin.login;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.shredzone.cilla.ws.client.RemoteLoginService;
import org.shredzone.cilla.ws.client.RemoteUserDetails;
import org.shredzone.cilla.ws.user.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 * This bean handles user authentication. It is used in the login and logout masks.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("request")
public class LoginBean implements Serializable {
    private static final long serialVersionUID = -191823661450034450L;

    private final transient Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private transient RemoteLoginService remoteLoginService;

    private String userName;
    private String password;

    /**
     * Username of the user trying to login.
     */
    @NotNull
    @Size(min = 1, max = 255)
    public String getUserName()                 { return userName; }
    public void setUserName(String userName)    { this.userName = userName; }

    /**
     * Password given by the user trying to login.
     */
    @NotNull
    @Size(min = 1, max = 255)
    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    /**
     * Tries to login the user with the given credentials.
     *
     * @return name of the admin index page if the login was successful, {@code null} if
     *         the login was not successful.
     */
    public String login() {
        try {
            remoteLoginService.login(getUserName(), getPassword());
        } catch (AuthenticationException ex) {
            log.error("Authentication failed, user: '" + getUserName() + "', password: '********'", ex);

            FacesContext ctx = FacesContext.getCurrentInstance();
            Locale loc = ctx.getViewRoot().getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), loc);
            FacesMessage message = new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            bundle.getString("login.failed"),
                            ex.getMessage()
            );
            ctx.addMessage(null, message);

            return null;
        }

        return "/admin/index.xhtml";
    }

    /**
     * Logs out the currently logged in user.
     *
     * @return name of the logout page to render
     */
    public String logout() {
        remoteLoginService.logout();
        return "/logout";
    }

    /**
     * Returns the currently logged in user.
     *
     * @return {@link UserDto} of the authenticated user, or {@code null} if not logged in
     */
    public UserDto getUser() {
        RemoteUserDetails details = remoteLoginService.getAuthenticatedUser();
        return (details != null ? details.getUser() : null);
    }

    /**
     * Returns the {@link TimeZone} of the currently logged in user. It's a convenience
     * call for {@link UserDto#getTimeZone()}.
     *
     * @return {@link TimeZone} of the authenticated user, or the system's default time
     *         zone if not logged in
     */
    public TimeZone getTimeZone() {
        UserDto user = getUser();
        return (user != null ? user.getTimeZone() : TimeZone.getDefault());
    }

}
