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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.activation.DataHandler;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.Geolocated;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.cilla.ws.TimeDefinition;
import org.shredzone.cilla.ws.adapter.TimeZoneAdapter;

/**
 * A Picture contained in a Gallery Section.
 *
 * @author Richard "Shred" Körber
 */
@XmlType
public class PictureDto extends BaseDto implements Geolocated {
    private static final long serialVersionUID = -3277725908745526672L;

    private Date createDate;
    private TimeZone createTimeZone;
    private TimeDefinition createTimeDefinition;
    private String caption;
    private TextFormat captionFormat;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal altitude;
    private DataHandler uploadFile;
    private List<String> tags = new ArrayList<>();
    private boolean commentable;
    private String hashId;

    public Date getCreateDate()                 { return createDate; }
    public void setCreateDate(Date createDate)  { this.createDate = createDate; }

    @XmlJavaTypeAdapter(TimeZoneAdapter.class)
    public TimeZone getCreateTimeZone()         { return createTimeZone; }
    public void setCreateTimeZone(TimeZone createTimeZone) { this.createTimeZone = createTimeZone; }

    public TimeDefinition getCreateTimeDefinition() { return createTimeDefinition; }
    public void setCreateTimeDefinition(TimeDefinition createTimeDefinition) { this.createTimeDefinition = createTimeDefinition; }

    public String getCaption()                  { return caption; }
    public void setCaption(String caption)      { this.caption = caption; }

    @NotNull
    public TextFormat getCaptionFormat()        { return captionFormat; }
    public void setCaptionFormat(TextFormat captionFormat) { this.captionFormat = captionFormat; }

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

    public List<String> getTags()               { return tags; }
    public void setTags(List<String> tags)      { this.tags = tags; }

    public boolean isCommentable()                  { return commentable; }
    public void setCommentable(boolean commentable) { this.commentable = commentable; }

    @XmlMimeType("application/octet-stream")
    public DataHandler getUploadFile()          { return uploadFile; }
    public void setUploadFile(DataHandler uploadFile) { this.uploadFile = uploadFile; }

    @NotNull
    public String getHashId()                   { return hashId; }
    public void setHashId(String hashId)        { this.hashId = hashId; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PictureDto && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
