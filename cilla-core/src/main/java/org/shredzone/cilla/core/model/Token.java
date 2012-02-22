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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * User dependent token or credentials for external web services.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "site"}))
public class Token extends BaseModel {
    private static final long serialVersionUID = 5515578271144384277L;

    private User user;
    private String site;
    private String token;
    private String secret;

    /**
     * {@link User} this token belongs to.
     */
    @ManyToOne(optional = false)
    public User getUser()                       { return user; }
    public void setUser(User user)              { this.user = user; }

    /**
     * Name of the site this token is used at.
     */
    @Column(nullable = false)
    public String getSite()                     { return site; }
    public void setSite(String site)            { this.site = site; }

    /**
     * The token itself.
     */
    @Column(nullable = false)
    public String getToken()                    { return token; }
    public void setToken(String token)          { this.token = token; }

    /**
     * A secret that is used together with the token.
     */
    @Column(nullable = false)
    public String getSecret()                   { return secret; }
    public void setSecret(String secret)        { this.secret = secret; }

}
