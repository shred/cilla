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
package org.shredzone.cilla.web.info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.shredzone.cilla.core.util.DateUtils;

/**
 * A flattened {@link PageComment} to be used for displaying.
 *
 * @author Richard "Shred" Körber
 */
public class PageComment implements Serializable {
    private static final long serialVersionUID = -785593274229823421L;

    private long id;
    private String name;
    private String mail;
    private String mailhash;
    private String url;
    private Date creation;
    private String text;
    private int level;
    private Long creatorId;
    private List<PageComment> children;

    /**
     * Comment ID.
     */
    public long getId()                         { return id; }
    public void setId(long id)                  { this.id = id; }

    /**
     * Commentor's plain name.
     */
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * Commentor's mail address.
     */
    public String getMail()                     { return mail; }
    public void setMail(String mail)            { this.mail = mail; }

    /**
     * MD5 hash of the mail address, to be used for Gravatar.
     */
    public String getMailhash()                 { return mailhash; }
    public void setMailhash(String mailhash)    { this.mailhash = mailhash; }

    /**
     * Commentor's URL.
     */
    public String getUrl()                      { return url; }
    public void setUrl(String url)              { this.url = url; }

    /**
     * Comment creation date.
     */
    public Date getCreation()                   { return DateUtils.cloneDate(creation); }
    public void setCreation(Date creation)      { this.creation = DateUtils.cloneDate(creation); }

    /**
     * Comment text, as HTML.
     */
    public String getText()                     { return text; }
    public void setText(String text)            { this.text = text; }

    /**
     * Comment level. Root comments are level 0, children are level 1, grandchildren are
     * level 2, and so on...
     */
    public int getLevel()                       { return level; }
    public void setLevel(int level)             { this.level = level; }

    /**
     * Commentor's user ID, if the comment was posted by a logged-in user.
     */
    public Long getCreatorId()                  { return creatorId; }
    public void setCreatorId(Long creatorId)    { this.creatorId = creatorId; }

    /**
     * Children {@link PageComment}. {@code null} if this is a leaf.
     */
    public List<PageComment> getChildren()      { return children; }
    public void setChildren(List<PageComment> children) { this.children = children; }

}
