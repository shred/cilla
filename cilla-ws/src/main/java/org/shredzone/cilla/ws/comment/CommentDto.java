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
package org.shredzone.cilla.ws.comment;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.TextFormat;

/**
 * A Comment.
 *
 * @author Richard "Shred" Körber
 */
public class CommentDto extends BaseDto {
    private static final long serialVersionUID = 7953129056497110805L;

    private long threadId;
    private Long replyToId;
    private boolean published;
    private String name;
    private String mail;
    private String url;
    private String text;
    private TextFormat textFormat;
    private Long creatorId;
    private String creatorLogin;
    private String creatorName;
    private Date creation;

    @NotNull
    public Date getCreation()               { return creation; }
    public void setCreation(Date creation)  { this.creation = creation; }

    public Long getCreatorId()              { return creatorId; }
    public void setCreatorId(Long creatorId) { this.creatorId = creatorId; }

    public String getCreatorLogin()         { return creatorLogin; }
    public void setCreatorLogin(String creatorLogin) { this.creatorLogin = creatorLogin; }

    public String getCreatorName()          { return creatorName; }
    public void setCreatorName(String creatorName) { this.creatorName = creatorName; }

    @Size(max = 255)
    public String getMail()                 { return mail; }
    public void setMail(String mail)        { this.mail = mail; }

    @Size(max = 255)
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    public long getThreadId()               { return threadId; }
    public void setThreadId(long threadId)  { this.threadId = threadId; }

    public boolean isPublished()            { return published; }
    public void setPublished(boolean published) { this.published = published; }

    public Long getReplyToId()              { return replyToId; }
    public void setReplyToId(Long replyToId) { this.replyToId = replyToId; }

    public String getText()                 { return text; }
    public void setText(String text)        { this.text = text; }

    @NotNull
    public TextFormat getTextFormat()       { return textFormat; }
    public void setTextFormat(TextFormat textFormat) { this.textFormat = textFormat; }

    @Size(max = 255)
    public String getUrl()                  { return url; }
    public void setUrl(String url)          { this.url = url; }

}
