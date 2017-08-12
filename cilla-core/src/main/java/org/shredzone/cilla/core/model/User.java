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
package org.shredzone.cilla.core.model;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A user.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Table(name = "Login") // "user" is a reserved word of some DBMS
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseModel {
    private static final long serialVersionUID = 5873021765092486197L;

    private String login;
    private String password;
    private String name;
    private String mail;
    private Role role;
    private Language language;
    private TimeZone timeZone;
    private Map<String, String> properties = new HashMap<>();

    /**
     * Login name of the user.
     */
    @Column(unique = true, nullable = false)
    public String getLogin()                    { return login; }
    public void setLogin(String login)          { this.login = login; }

    /**
     * Password of the user, encrypted and salted.
     */
    @Column(nullable = false)
    public String getPassword()                 { return password; }
    public void setPassword(String password)    { this.password = password; }

    /**
     * Full name of the user.
     */
    @Column(nullable = false)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * Mail address of the user.
     */
    @Column(nullable = false)
    public String getMail()                     { return mail; }
    public void setMail(String mail)            { this.mail = mail; }

    /**
     * Role this user belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public Role getRole()                       { return role; }
    public void setRole(Role role)              { this.role = role; }

    /**
     * Language of the user.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Language getLanguage()               { return language; }
    public void setLanguage(Language language)  { this.language = language; }

    /**
     * Time zone the user lives in.
     */
    @Column(nullable = false)
    public TimeZone getTimeZone()               { return timeZone; }
    public void setTimeZone(TimeZone timeZone)  { this.timeZone = timeZone; }

    /**
     * Common user properties.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "User_Properties")
    @MapKeyColumn(name = "key")
    @Column(name = "value", columnDefinition = "text")
    public Map<String, String> getProperties()  { return properties; }
    public void setProperties(Map<String, String> properties) { this.properties = properties; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof User && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
