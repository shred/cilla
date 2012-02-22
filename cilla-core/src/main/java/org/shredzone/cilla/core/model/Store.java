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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.shredzone.cilla.core.util.DateUtils;

/**
 * A {@link Store} entity represents a stored file that can be used in different places.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Store extends BaseModel {
    private static final long serialVersionUID = -7902019312112064012L;

    private String name;
    private String contentType;
    private Date lastModified;

    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    @Column(nullable = false)
    public String getContentType()              { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getLastModified()               { return DateUtils.cloneDate(lastModified); }
    public void setLastModified(Date lastModified) { this.lastModified = DateUtils.cloneDate(lastModified); }

}
