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
import java.util.TimeZone;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.shredzone.cilla.ws.TimeDefinition;

/**
 * A section containing a gallery.
 *
 * @author Richard "Shred" Körber
 */
@Entity
public class GallerySection extends Section {
    private static final long serialVersionUID = 5071579692034235164L;

    private List<Picture> pictures = new ArrayList<Picture>();
    private TimeZone defaultTimeZone;
    private TimeDefinition defaultTimePrecision;

    /**
     * List of {@link Picture} of this gallery.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "gallery")
    @OrderBy("sequence")
    public List<Picture> getPictures()          { return pictures; }
    public void setPictures(List<Picture> pictures) { this.pictures = pictures; }

    /**
     * Default timezone that is used in this gallery. It should contain the time zone of
     * the place where the pictures have been taken.
     */
    @Column(nullable = false)
    public TimeZone getDefaultTimeZone()        { return defaultTimeZone; }
    public void setDefaultTimeZone(TimeZone defaultTimeZone) { this.defaultTimeZone = defaultTimeZone; }

    /**
     * Default time precision that is used in this gallery. For photos taken by an analog
     * camera, often only the month and year is known.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    public TimeDefinition getDefaultTimePrecision() { return defaultTimePrecision; }
    public void setDefaultTimePrecision(TimeDefinition defaultTimePrecision) { this.defaultTimePrecision = defaultTimePrecision; }

    @Override
    @Transient
    public String getType()                     { return "gallery"; }

}
