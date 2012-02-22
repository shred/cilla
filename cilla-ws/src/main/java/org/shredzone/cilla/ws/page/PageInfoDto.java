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

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.shredzone.cilla.ws.BaseDto;

/**
 * Contains basic information about a Page.
 *
 * @author Richard "Shred" Körber
 * @see PageDto
 */
public class PageInfoDto extends BaseDto {
    private static final long serialVersionUID = -197373824299901427L;

    private String name;
    private String title;
    private String subject;
    private long languageId;
    private long creatorId;
    private String creatorLogin;
    private String creatorName;
    private Date creation;
    private Date modification;
    private Date publication;
    private Date expiration;
    private boolean sticky;
    private boolean hidden;
    private boolean commentable;
    private boolean flattrable;

    @Size(min = 0, max = 50)
    public String getName()                         { return name; }
    public void setName(String name)                { this.name = name; }

    @NotNull
    @Size(min = 1, max = 255)
    public String getTitle()                        { return title; }
    public void setTitle(String title)              { this.title = title; }

    @Size(min = 0, max = 255)
    public String getSubject()                      { return subject; }
    public void setSubject(String subject)          { this.subject = subject; }

    public long getCreatorId()                      { return creatorId; }
    public void setCreatorId(long creatorId)        { this.creatorId = creatorId; }

    public long getLanguageId()                     { return languageId; }
    public void setLanguageId(long languageId)      { this.languageId = languageId; }

    public String getCreatorLogin()                 { return creatorLogin; }
    public void setCreatorLogin(String creatorLogin) { this.creatorLogin = creatorLogin; }

    public String getCreatorName()                  { return creatorName; }
    public void setCreatorName(String creatorName)  { this.creatorName = creatorName; }

    @NotNull
    @Past
    public Date getCreation()                       { return creation; }
    public void setCreation(Date creation)          { this.creation = creation; }

    @NotNull
    public Date getModification()                   { return modification; }
    public void setModification(Date modification)  { this.modification = modification; }

    public Date getPublication()                    { return publication; }
    public void setPublication(Date publication)    { this.publication = publication; }

    public Date getExpiration()                     { return expiration; }
    public void setExpiration(Date expiration)      { this.expiration = expiration; }

    public boolean isSticky()                       { return sticky; }
    public void setSticky(boolean sticky)           { this.sticky = sticky; }

    public boolean isHidden()                       { return hidden; }
    public void setHidden(boolean hidden)           { this.hidden = hidden; }

    public boolean isCommentable()                  { return commentable; }
    public void setCommentable(boolean commentable) { this.commentable = commentable; }

    public boolean isFlattrable()                   { return flattrable; }
    public void setFlattrable(boolean flattrable)   { this.flattrable = flattrable; }

    public boolean isPublished() {
        return (publication != null && publication.before(new Date()));
    }

    public boolean isVisible() {
        return isPublished() && (expiration == null || expiration.after(new Date()));
    }

}
