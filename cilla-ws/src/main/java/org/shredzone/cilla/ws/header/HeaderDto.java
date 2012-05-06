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

package org.shredzone.cilla.ws.header;

import java.math.BigDecimal;
import java.util.Date;

import javax.activation.DataHandler;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.Geolocated;
import org.shredzone.cilla.ws.TextFormat;

/**
 * A Header is a random image shown at the head of the blog.
 *
 * @author Richard "Shred" Körber
 */
@XmlType
public class HeaderDto extends BaseDto implements Geolocated {
    private static final long serialVersionUID = 5983186325362814767L;

    private String name;
    private String caption;
    private String description;
    private TextFormat descriptionFormat;
    private Date creation;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal altitude;
    private boolean commentable;
    private boolean enabled;
    private long creatorId;
    private String creatorLogin;
    private String creatorName;
    private DataHandler uploadHeaderFile;
    private DataHandler uploadFullFile;

    @NotNull
    @Size(min = 1, max = 255)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public String getCaption()                  { return caption; }
    public void setCaption(String caption)      { this.caption = caption; }

    public String getDescription()              { return description; }
    public void setDescription(String description) { this.description = description; }

    @NotNull
    public TextFormat getDescriptionFormat()    { return descriptionFormat; }
    public void setDescriptionFormat(TextFormat descriptionFormat) { this.descriptionFormat = descriptionFormat; }

    @NotNull
    public Date getCreation()                   { return creation; }
    public void setCreation(Date creation)      { this.creation = creation; }

    @Override
    public BigDecimal getLongitude()            { return longitude; }
    @Override
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

    @Override
    public BigDecimal getLatitude()             { return latitude; }
    @Override
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    @Override
    public BigDecimal getAltitude()             { return altitude; }
    @Override
    public void setAltitude(BigDecimal altitude) { this.altitude = altitude; }

    public boolean isCommentable()              { return commentable; }
    public void setCommentable(boolean commentable) { this.commentable = commentable; }

    public boolean isEnabled()                  { return enabled; }
    public void setEnabled(boolean enabled)     { this.enabled = enabled; }

    public long getCreatorId()                  { return creatorId; }
    public void setCreatorId(long creatorId)    { this.creatorId = creatorId; }

    public String getCreatorLogin()             { return creatorLogin; }
    public void setCreatorLogin(String creatorLogin) { this.creatorLogin = creatorLogin; }

    public String getCreatorName()              { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    @XmlMimeType("application/octet-stream")
    public DataHandler getUploadHeaderFile()    { return uploadHeaderFile; }
    public void setUploadHeaderFile(DataHandler uploadHeaderFile) { this.uploadHeaderFile = uploadHeaderFile; }

    @XmlMimeType("application/octet-stream")
    public DataHandler getUploadFullFile()      { return uploadFullFile; }
    public void setUploadFullFile(DataHandler uploadFullFile) { this.uploadFullFile = uploadFullFile; }

}
