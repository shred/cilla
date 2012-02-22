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
package org.shredzone.cilla.ws.user;

import java.util.Locale;
import java.util.TimeZone;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.adapter.LocaleAdapter;
import org.shredzone.cilla.ws.adapter.TimeZoneAdapter;

/**
 * Represents a User.
 *
 * @author Richard "Shred" Körber
 */
public class UserDto extends BaseDto {
    private static final long serialVersionUID = 5584978288821397187L;

    private String login;
    private String name;
    private String mail;
    private String roleName;
    private long languageId;
    private Locale language;
    private TimeZone timeZone;

    @NotNull
    @Size(min = 1, max = 255)
    public String getLogin()                    { return login; }
    public void setLogin(String login)          { this.login = login; }

    @NotNull
    @Size(min = 1, max = 255)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    @Size(max = 255)
    public String getMail()                     { return mail; }
    public void setMail(String mail)            { this.mail = mail; }

    @Size(max = 255)
    public String getRoleName()                 { return roleName; }
    public void setRoleName(String roleName)    { this.roleName = roleName; }

    public long getLanguageId()                 { return languageId; }
    public void setLanguageId(long languageId)  { this.languageId = languageId; }

    @XmlJavaTypeAdapter(LocaleAdapter.class)
    public Locale getLanguage()                 { return language; }
    public void setLanguage(Locale language)    { this.language = language; }

    @XmlJavaTypeAdapter(TimeZoneAdapter.class)
    public TimeZone getTimeZone()               { return timeZone; }
    public void setTimeZone(TimeZone timeZone)  { this.timeZone = timeZone; }

}
