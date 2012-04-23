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
package org.shredzone.cilla.web.renderer;

import java.io.IOException;
import java.util.Locale;

import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.web.comment.CommentThreadModel;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;

/**
 * An abstract implementation of {@link CommentRendererStrategy} that does almost nothing.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractCommentRendererStrategy implements CommentRendererStrategy {

    protected Locale locale;
    protected Commentable commentable;
    protected CommentThreadModel thread;
    protected FragmentContext context;

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public void setCommentable(Commentable commentable) {
        this.commentable = commentable;
    }

    @Override
    public void setThread(CommentThreadModel thread) {
        this.thread = thread;
    }

    @Override
    public void setContext(FragmentContext context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void commentsDisallowed(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void openContainer(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void noCommentHeader(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void openCommentBox(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void closeCommentBox(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void closeContainer(Appendable out) throws IOException {
        // Do nothing by default...
    }

}
