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

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.model.embed.Geolocation;
import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.core.util.DateUtils;

/**
 * A header is an image that is shown at the top of the website.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Header extends BaseModel implements Commentable {
    private static final long serialVersionUID = -4930167448389893596L;

    private String name;
    private String caption;
    private FormattedText description;
    private Geolocation location;
    private int width;
    private int height;
    private User creator;
    private Date creation;
    private Store headerImage = new Store();
    private Store fullImage = new Store();
    private CommentThread thread = new CommentThread();
    private boolean enabled;

    /**
     * Header name
     */
    @Column(nullable = false)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * Caption: a short one-liner without markup
     */
    public String getCaption()                  { return caption; }
    public void setCaption(String caption)      { this.caption = caption; }

    /**
     * A page-filling long description about the image.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "text", column = @Column(name = "description", columnDefinition = "text")),
        @AttributeOverride(name = "format", column = @Column(name = "descriptionFormat"))
    })
    public FormattedText getDescription()       { return description; }
    public void setDescription(FormattedText description) { this.description = description; }

    /**
     * Location where the picture was taken, or {@code null}.
     */
    @Embedded
    public Geolocation getLocation()            { return location; }
    public void setLocation(Geolocation location) { this.location = location; }

    /**
     * Width of the header image.
     */
    @Column(nullable = false)
    public int getWidth()                       { return width; }
    public void setWidth(int width)             { this.width = width; }

    /**
     * Height of the header image.
     */
    @Column(nullable = false)
    public int getHeight()                      { return height; }
    public void setHeight(int height)           { this.height = height; }

    /**
     * Creator {@link User} of this page.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public User getCreator()                    { return creator; }
    public void setCreator(User creator)        { this.creator = creator;}

    /**
     * Creation date.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreation()                   { return DateUtils.cloneDate(creation); }
    public void setCreation(Date creation)      { this.creation = DateUtils.cloneDate(creation); }

    /**
     * Header image resource.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Store getHeaderImage()               { return headerImage; }
    public void setHeaderImage(Store headerImage) { this.headerImage = headerImage; }

    /**
     * Full image resource.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Store getFullImage()                 { return fullImage; }
    public void setFullImage(Store fullImage)   { this.fullImage = fullImage; }

    /**
     * If {@code true}, this header image is eligible for use. If there is more than one
     * header image enabled, the header is randomly chosen for each request.
     */
    public boolean isEnabled()                  { return enabled; }
    public void setEnabled(boolean enabled)     { this.enabled = enabled; }

    /**
     * Comment thread.
     */
    @Override
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public CommentThread getThread()            { return thread; }
    public void setThread(CommentThread thread) { this.thread = thread; }

}
