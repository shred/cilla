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

import javax.activation.DataHandler;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlType;

import org.shredzone.cilla.ws.BaseDto;

/**
 * A Medium used in a Page.
 *
 * @author Richard "Shred" Körber
 */
@XmlType
public class MediumDto extends BaseDto {
    private static final long serialVersionUID = 5803185887549169684L;

    private String name;
    private long creatorId;
    private String creatorLogin;
    private String creatorName;
    private DataHandler uploadMediumFile;

    @NotNull
    @Size(min = 1, max = 255)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    public long getCreatorId()                  { return creatorId; }
    public void setCreatorId(long creatorId)    { this.creatorId = creatorId; }

    public String getCreatorLogin()             { return creatorLogin; }
    public void setCreatorLogin(String creatorLogin) { this.creatorLogin = creatorLogin; }

    public String getCreatorName()              { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    @XmlMimeType("application/octet-stream")
    public DataHandler getUploadMediumFile()    { return uploadMediumFile; }
    public void setUploadMediumFile(DataHandler uploadMediumFile) { this.uploadMediumFile = uploadMediumFile; }

}
