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
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Index;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.util.DateUtils;

/**
 * A single comment for a {@link Page} or one of its sections.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "countAllComments", query = "SELECT COUNT(*) FROM Comment")
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment extends BaseModel {
    private static final long serialVersionUID = -976044775653519085L;

    private Page page;
    private Long sectionRef;
    private Comment replyTo;
    private boolean published;
    private String name;
    private String mail;
    private String url;
    private FormattedText text;
    private User creator;
    private Date creation;

    /**
     * {@link Page} this comment comments on.
     */
    @ManyToOne(optional = false)
    @Index(name = "idx_comment_page")
    public Page getPage()                       { return page; }
    public void setPage(Page page)              { this.page = page; }

    /**
     * A section internal reference id, for comments that are to be shown only on a
     * certain part of a section. May be {@code null}. If not {@code null}, the comment
     * will not be shown on the standard page view.
     */
    public Long getSectionRef()                 { return sectionRef; }
    public void setSectionRef(Long sectionRef)  { this.sectionRef = sectionRef; }

    /**
     * Reference to the {@link Comment} this {@link Comment} replies to.
     */
    @ManyToOne
    public Comment getReplyTo()                 { return replyTo; }
    public void setReplyTo(Comment replyTo)     { this.replyTo = replyTo; }

    /**
     * Is the comment visible? {@code false} means that the comment awaits moderation.
     */
    public boolean isPublished()                { return published; }
    public void setPublished(boolean published) { this.published = published; }

    /**
     * The name of the commentor. May be {@code null} for anonymous comments.
     */
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * The email of the commentor. May be {@code null} for anonymous comments.
     */
    public String getMail()                     { return mail; }
    public void setMail(String mail)            { this.mail = mail; }

    /**
     * The URL of the commentor. May be {@code null}. For trackback comments, this is the
     * origin site of the trackback.
     */
    public String getUrl()                      { return url; }
    public void setUrl(String url)              { this.url = url; }

    /**
     * The comment text.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "text", column = @Column(name = "text", nullable = false)),
        @AttributeOverride(name = "format", column = @Column(name = "textFormat", nullable = false))
    })
    public FormattedText getText()              { return text; }
    public void setText(FormattedText text)     { this.text = text; }

    /**
     * The creator of this comment. Overrides the name property if set! {@code null} means
     * that the commentor was not logged in.
     */
    @ManyToOne
    public User getCreator()                    { return creator; }
    public void setCreator(User creator)        { this.creator = creator; }

    /**
     * Creation date of the comment.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreation()                   { return DateUtils.cloneDate(creation); }
    public void setCreation(Date creation)      { this.creation = DateUtils.cloneDate(creation); }

}
