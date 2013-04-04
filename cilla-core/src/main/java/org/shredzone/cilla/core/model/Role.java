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

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A role. This is a set of rights (authorities) that can be assigned to individual users.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseModel {
    private static final long serialVersionUID = 8687576118285221859L;

    private String name;
    private Collection<Authority> authorities = new HashSet<>();

    /**
     * Descriptive name.
     */
    @Column(nullable = false, unique = true)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * Authorities of this role.
     */
    @ManyToMany
    public Collection<Authority> getAuthorities() { return authorities; }
    public void setAuthorities(Collection<Authority> authorities) { this.authorities = authorities; }

}
