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

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A single medium of a {@link Page}. This is an photo or illustration to be used within
 * the page.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Medium extends BaseModel {
    private static final long serialVersionUID = 7600602175440503860L;

    private Page page;
    private User createdBy;
    private Store image = new Store();

    /**
     * The {@link Page} this medium belongs to.
     */
    @ManyToOne(optional = false)
    public Page getPage()                       { return page; }
    public void setPage(Page page)              { this.page = page; }

    /**
     * {@link User} who created that medium.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public User getCreatedBy()                  { return createdBy; }
    public void setCreatedBy(User createdBy)    { this.createdBy = createdBy; }

    /**
     * Image resource.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Store getImage()                     { return image; }
    public void setImage(Store image)           { this.image = image; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Medium && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
