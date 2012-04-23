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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A thread of comments.
 *
 * @author Richard "Shred" Körber
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentThread extends BaseModel {
    private static final long serialVersionUID = 2266998031115838916L;

    private List<Comment> comments = new ArrayList<Comment>();
    private boolean commentable;

    /**
     * All {@link Comment} of this thread.
     */
    @OneToMany(mappedBy = "thread", fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("creation")
    public List<Comment> getComments()          { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    /**
     * {@code true} if comments are enabled for this thread. If set to {@code false}, the
     * thread does not accept comments.
     */
    @Column(nullable = false)
    public boolean isCommentable()              { return commentable; }
    public void setCommentable(boolean commentable) { this.commentable = commentable; }

}
