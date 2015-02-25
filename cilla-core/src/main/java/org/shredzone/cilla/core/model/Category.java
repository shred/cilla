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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.shredzone.cilla.core.model.embed.FormattedText;

/**
 * A Category.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Category extends BaseModel {
    private static final long serialVersionUID = 502091773587359973L;

    private Category parent;
    private List<Category> children = new ArrayList<Category>();
    private int sequence;
    private String name;
    private String title;
    private String icon;
    private FormattedText caption;

    /**
     * The parent {@link Category}, or {@code null} if this is a root category.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Category getParent()                 { return parent; }
    public void setParent(Category parent)      { this.parent = parent; }

    /**
     * A list of sub-categories of this {@link Category}.
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("sequence")
    public List<Category> getChildren()         { return children; }
    public void setChildren(List<Category> children) { this.children = children; }

    /**
     * Order of appearance.
     */
    public int getSequence()                    { return sequence; }
    public void setSequence(int sequence)       { this.sequence = sequence; }

    /**
     * The category name.
     */
    @Column(unique = true, nullable = false)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * A short description of the category to be shown in tooltips.
     */
    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    /**
     * The location of an icon representing the category. The location depends on the
     * frontend. It may be an absolute URL, or some relative path.
     */
    public String getIcon()                     { return icon; }
    public void setIcon(String icon)            { this.icon = icon; }

    /**
     * Category caption text, optional.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "text", column = @Column(name = "caption")),
        @AttributeOverride(name = "format", column = @Column(name = "captionFormat"))
    })
    public FormattedText getCaption()           { return caption; }
    public void setCaption(FormattedText caption) { this.caption = caption; }

}
