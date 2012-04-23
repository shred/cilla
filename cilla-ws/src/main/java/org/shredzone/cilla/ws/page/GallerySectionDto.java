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
package org.shredzone.cilla.ws.page;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.shredzone.cilla.ws.TimeDefinition;
import org.shredzone.cilla.ws.adapter.TimeZoneAdapter;

/**
 * A Gallery Section.
 *
 * @author Richard "Shred" Körber
 */
@XmlRootElement
public class GallerySectionDto extends SectionDto {
    private static final long serialVersionUID = -2124978490411976621L;

    private TimeZone defaultTimeZone;
    private TimeDefinition defaultTimePrecision;
    private List<PictureDto> pictures = new ArrayList<PictureDto>();
    private boolean commentable;

    @Override
    public String getType()                     { return "gallery"; }

    @NotNull
    @XmlJavaTypeAdapter(TimeZoneAdapter.class)
    public TimeZone getDefaultTimeZone()        { return defaultTimeZone; }
    public void setDefaultTimeZone(TimeZone defaultTimeZone) { this.defaultTimeZone = defaultTimeZone; }

    @NotNull
    public TimeDefinition getDefaultTimePrecision() { return defaultTimePrecision; }
    public void setDefaultTimePrecision(TimeDefinition defaultTimePrecision) { this.defaultTimePrecision = defaultTimePrecision; }

    public List<PictureDto> getPictures()       { return pictures;}
    public void setPictures(List<PictureDto> pictures) { this.pictures = pictures; }

    public boolean isCommentable()              { return commentable; }
    public void setCommentable(boolean commentable) { this.commentable = commentable; }

}
