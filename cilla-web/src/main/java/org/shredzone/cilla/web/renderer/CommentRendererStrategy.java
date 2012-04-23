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
import org.shredzone.cilla.web.comment.CommentModel;
import org.shredzone.cilla.web.comment.CommentThreadModel;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;

/**
 * A renderer strategy for rendering comments.
 *
 * @author Richard "Shred" Körber
 */
public interface CommentRendererStrategy {

    /**
     * Sets the {@link Locale} to be used in this strategy.
     */
    void setLocale(Locale locale);

    /**
     * Sets the {@link Commentable} this comment refers to.
     */
    void setCommentable(Commentable commentable);

    /**
     * Sets the {@link CommentThreadModel} containing the comments.
     */
    void setThread(CommentThreadModel thread);

    /**
     * Sets the {@link FragmentContext} of this invocation.
     */
    void setContext(FragmentContext context);

    /**
     * Writes a HTML section that states that there are no comments, and new comments are
     * also not accepted. This is written when the {@link Commentable} does not accept
     * comments and there were also no comments added in the past.
     */
    void commentsDisallowed(Appendable out) throws IOException;

    /**
     * Opens the HTML container of the strategy.
     *
     * @param out
     *            Target to write to
     */
    void openContainer(Appendable out) throws IOException;

    /**
     * Writes a text stating that the commentable was not commented yet.
     *
     * @param out
     *            Target to write to
     */
    void noCommentHeader(Appendable out) throws IOException;

    /**
     * Opens the HTML container for the list of comments.
     *
     * @param out
     *            Target to write to
     */
    void openCommentBox(Appendable out) throws IOException;

    /**
     * Writes a single comment.
     *
     * @param comment
     *            Comment to be written
     * @param out
     *            Target to write to
     */
    void comment(CommentModel comment, Appendable out) throws IOException;

    /**
     * Closes the HTML container for the list of comments.
     *
     * @param out
     *            Target to write to
     */
    void closeCommentBox(Appendable out) throws IOException;

    /**
     * Writes a form that is used for posting new comments.
     *
     * @param out
     *            Target to write to
     */
    void commentForm(Appendable out) throws IOException;

    /**
     * Closes the HTML container of the strategy.
     *
     * @param out
     *            Target to write to
     */
    void closeContainer(Appendable out) throws IOException;

}
