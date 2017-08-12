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

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A single {@link Section} of a {@link Page}. This is an abstract superclass. Each
 * section type needs a separate implementation.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "countSections", query = "SELECT COUNT(*) FROM Section WHERE page=:page")
})
@Inheritance(strategy = InheritanceType.JOINED)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(indexes = {@Index(name = "idx_section_page", columnList = "page_id")})
public abstract class Section extends BaseModel {
    private static final long serialVersionUID = 5882612665202288874L;

    private Page page;
    private int sequence;

    /**
     * {@link Page} this section belongs to.
     */
    @ManyToOne(optional = false)
    public Page getPage()                       { return page; }
    public void setPage(Page page)              { this.page = page; }

    /**
     * Position of this section within the {@link Page}.
     */
    public int getSequence()                    { return sequence; }
    public void setSequence(int sequence)       { this.sequence = sequence; }

    /**
     * Section type.
     */
    @Transient
    public abstract String getType();

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Section && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
