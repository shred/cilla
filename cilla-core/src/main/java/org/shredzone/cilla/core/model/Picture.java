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
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Sort;
import org.hibernate.annotations.SortType;
import org.shredzone.cilla.core.model.embed.ExifData;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.model.embed.Geolocation;
import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.core.util.DateUtils;
import org.shredzone.cilla.ws.TimeDefinition;

/**
 * Stores a single picture for a {@link GallerySection}.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Picture extends BaseModel implements Commentable {
    private static final long serialVersionUID = 942950890144760003L;

    private GallerySection gallery;
    private int sequence;
    private int width;
    private int height;
    private Date createDate;
    private TimeZone createTimeZone;
    private TimeDefinition createTimeDefinition;
    private FormattedText caption;
    private Geolocation location;
    private ExifData exifData;
    private Store image = new Store();
    private SortedSet<Tag> tags = new TreeSet<Tag>();
    private CommentThread thread = new CommentThread();

    /**
     * {@link GallerySection} this picture belongs to.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    public GallerySection getGallery()          { return gallery; }
    public void setGallery(GallerySection gallery) { this.gallery = gallery; }

    /**
     * Position of this picture in the gallery.
     */
    @Column(nullable = false)
    public int getSequence()                    { return sequence; }
    public void setSequence(int sequence)       { this.sequence = sequence; }

    /**
     * Image width.
     */
    @Column(nullable = false)
    public int getWidth()                       { return width; }
    public void setWidth(int width)             { this.width = width; }

    /**
     * Image height.
     */
    @Column(nullable = false)
    public int getHeight()                      { return height; }
    public void setHeight(int height)           { this.height = height; }

    /**
     * Creation date.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate()                 { return DateUtils.cloneDate(createDate); }
    public void setCreateDate(Date createDate)  { this.createDate = DateUtils.cloneDate(createDate); }

    /**
     * Time zone the picture was taken in.
     */
    @Column
    public TimeZone getCreateTimeZone()         { return createTimeZone; }
    public void setCreateTimeZone(TimeZone createTimeZone) { this.createTimeZone = createTimeZone; }

    /**
     * Time definition to be used for this picture.
     */
    @Column
    @Enumerated(EnumType.ORDINAL)
    public TimeDefinition getCreateTimeDefinition() { return createTimeDefinition; }
    public void setCreateTimeDefinition(TimeDefinition createTimeDefinition) { this.createTimeDefinition = createTimeDefinition; }

    /**
     * The caption rendered below this picture.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "text", column = @Column(name = "caption")),
        @AttributeOverride(name = "format", column = @Column(name = "captionFormat"))
    })
    public FormattedText getCaption()           { return caption; }
    public void setCaption(FormattedText caption) { this.caption = caption; }

    /**
     * Location the picture was taken at. {@code null} if the location is unknown or not
     * applicable.
     */
    @Embedded
    public Geolocation getLocation()            { return location; }
    public void setLocation(Geolocation location) { this.location = location; }

    /**
     * Exif data of this picture. {@code null} if there are no exif data available.
     */
    @Embedded
    public ExifData getExifData()               { return exifData; }
    public void setExifData(ExifData exifData)  { this.exifData = exifData; }

    /**
     * Image resource.
     */
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Store getImage()                     { return image; }
    public void setImage(Store image)           { this.image = image; }

    /**
     * All {@link Tag} this page are tagged with.
     */
    @ManyToMany
    @Sort(type = SortType.NATURAL)
    public SortedSet<Tag> getTags()             { return tags; }
    public void setTags(SortedSet<Tag> tags)    { this.tags = tags; }

    /**
     * Comment thread.
     */
    @Override
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public CommentThread getThread()            { return thread; }
    public void setThread(CommentThread thread) { this.thread = thread; }

}
