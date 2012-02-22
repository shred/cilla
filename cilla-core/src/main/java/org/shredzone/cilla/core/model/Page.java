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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.shredzone.cilla.core.model.embed.FlattrThing;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.util.DateUtils;

/**
 * A {@link Page} entity represents a single blog article to be displayed. It consists of
 * a title, a teaser text and a sequence of {@link Section}. Additionally, pages can be
 * tagged and assigned to different categories.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Page extends BaseModel {
    private static final long serialVersionUID = 1887769955448340721L;

    private Set<Category> categories = new HashSet<Category>();
    private Set<Tag> tags = new HashSet<Tag>();
    private List<Section> sections = new ArrayList<Section>();
    private String name;
    private String subject;
    private String title;
    private FormattedText teaser;
    private User creator;
    private Date creation;
    private Date modification;
    private Date publication;
    private Date expiration;
    private Language language;
    private boolean sticky;
    private boolean hidden;
    private boolean commentable;
    private boolean flattrable;
    private boolean publishedState;
    private String challenge;
    private String responsePattern;
    private FlattrThing flattr;

    /**
     * All {@link Category} this page belongs to.
     */
    @ManyToMany
    public Set<Category> getCategories()        { return categories; }
    public void setCategories(Set<Category> categories) { this.categories = categories; }

    /**
     * All {@link Tag} this page are tagged with.
     */
    @ManyToMany
    public Set<Tag> getTags()                   { return tags; }
    public void setTags(Set<Tag> tags)          { this.tags = tags; }

    /**
     * All {@link Section} of this page.
     */
    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY)
    @OrderBy("sequence")
    public List<Section> getSections()          { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }

    /**
     * {@code true} if the page is sticky. Sticky pages are always listed before
     * non-sticky pages.
     */
    @Column(nullable = false)
    public boolean isSticky()                   { return sticky; }
    public void setSticky(boolean sticky)       { this.sticky = sticky; }

    /**
     * {@code true} if the page is hidden. Hidden pages can only be accessed directly by
     * their URL or by a search, but will not appear in lists.
     */
    @Column(nullable = false)
    public boolean isHidden()                   { return hidden; }
    public void setHidden(boolean hidden)       { this.hidden = hidden; }

    /**
     * {@code true} if comments are enabled for this page. If set to {@code false}, the
     * page does not accept comments, nor shows comments that were previously posted.
     */
    @Column(nullable = false)
    public boolean isCommentable()              { return commentable; }
    public void setCommentable(boolean commentable) { this.commentable = commentable; }

    /**
     * {@code true} if the page is published, {@code false} if not published. This is a
     * status flag that is automatically changed by a service. Do not change it yourself.
     */
    @Column(nullable = false)
    public boolean isPublishedState()           { return publishedState; }
    public void setPublishedState(boolean publishedState) { this.publishedState = publishedState; }

    /**
     * Page name. {@code null} means there is no explicit name.
     */
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    /**
     * Page subject. All pages with the same subject are connected. {@code null} means
     * there is no explicit subject.
     */
    public String getSubject()                  { return subject; }
    public void setSubject(String subject)      { this.subject = subject; }

    /**
     * Page title.
     */
    @Column(nullable = false)
    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    /**
     * Page teaser. The teaser is the main text of the page, which is also shown in page
     * lists.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "text", column = @Column(name = "teaser", nullable = false)),
        @AttributeOverride(name = "format", column = @Column(name = "teaserFormat", nullable = false))
    })
    public FormattedText getTeaser()            { return teaser; }
    public void setTeaser(FormattedText teaser) { this.teaser = teaser; }

    /**
     * Creator {@link User} of this page.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public User getCreator()                    { return creator; }
    public void setCreator(User creator)        { this.creator = creator;}

    /**
     * Creation date.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreation()                   { return DateUtils.cloneDate(creation); }
    public void setCreation(Date creation)      { this.creation = DateUtils.cloneDate(creation); }

    /**
     * Last modification date.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getModification()               { return DateUtils.cloneDate(modification); }
    public void setModification(Date modification) { this.modification = DateUtils.cloneDate(modification); }

    /**
     * Publication date. The page is not visible prior to this date. {@code null} means
     * that the page is never visible (unpublished).
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getPublication()                { return DateUtils.cloneDate(publication); }
    public void setPublication(Date publication) { this.publication = DateUtils.cloneDate(publication); }

    /**
     * Expiration date. The page is not visible past this date. {@code null} means that
     * the page does not expire.
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date getExpiration()                 { return DateUtils.cloneDate(expiration); }
    public void setExpiration(Date expiration)  { this.expiration = DateUtils.cloneDate(expiration); }

    /**
     * {@link Language} the page content is written in
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Language getLanguage()               { return language; }
    public void setLanguage(Language language)  { this.language = language;}

    /**
     * Question that is shown to the visitor. If not {@code null}, this page is
     * restricted. {@link #setResponsePattern(String)} must also be set!
     */
    public String getChallenge()                { return challenge; }
    public void setChallenge(String challenge)  { this.challenge = challenge; }

    /**
     * Regular expression for the answer the visitor is expected to give for the question.
     * If not {@code null}, this page is restricted. {@link #setChallenge(String)} must
     * also be set!
     */
    public String getResponsePattern()          { return responsePattern; }
    public void setResponsePattern(String responsePattern) { this.responsePattern = responsePattern; }

    /**
     * {@code true} if the page can be flattr-ed. The page will automatically be
     * registered with flattr, and a flattr button is rendered.
     */
    @Column(nullable = false)
    public boolean isFlattrable()               { return flattrable; }
    public void setFlattrable(boolean flattrable) { this.flattrable = flattrable; }

    /**
     * Reference to the flattr registration.
     */
    public FlattrThing getFlattr()              { return flattr; }
    public void setFlattr(FlattrThing flattr)   { this.flattr = flattr; }


    /**
     * {@code true} if the page is currently published. A page is published if a
     * publication date is set and in the past, and an expiration date is either not set
     * or in the future.
     */
    @Transient
    public boolean isPublished() {
        Date now = new Date();
        return getPublication() != null && getPublication().before(now)
                && (getExpiration() == null || getExpiration().after(now));
    }

    /**
     * {@code true} if the access to the page is restricted. Restricted pages are only
     * shown after the visitor answered a question.
     */
    @Transient
    public boolean isRestricted() {
        return (challenge != null && responsePattern != null);
    }
}
