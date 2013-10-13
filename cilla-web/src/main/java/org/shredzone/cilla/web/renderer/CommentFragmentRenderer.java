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

import javax.annotation.Resource;
import javax.servlet.jsp.JspWriter;

import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.web.comment.CommentModel;
import org.shredzone.cilla.web.comment.CommentThreadModel;
import org.shredzone.cilla.web.comment.CommentThreadService;
import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentItem;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.web.fragment.annotation.FragmentValue;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * A fragment for rendering {@link Commentable}.
 *
 * @author Richard "Shred" Körber
 */
@Component
@FragmentRenderer
public class CommentFragmentRenderer {

    private @Resource ApplicationContext applicationContext;
    private @Resource CommentThreadService commentThreadService;

    /**
     * Renders a comment box.
     *
     * @param commentable
     *            {@link Commentable} to be commented
     * @param enabled
     *            {@code true} if commenting is enabled
     * @param locale
     *            {@link Locale} of the comment
     * @param context
     *            {@link FragmentContext} of the commenting fragment
     * @param out
     *            {@link JspWriter} to write the comment to
     */
    @Fragment(name = "comment")
    public void commentFragment(
        @FragmentItem Commentable commentable,
        @FragmentValue("#enabled") Boolean enabled,
        Locale locale,
        FragmentContext context,
        JspWriter out
    ) throws CillaServiceException, IOException {
        CommentThreadModel thread = commentThreadService.getCommentThread(commentable);

        CommentRendererStrategy strategy = getCommentRendererStrategy();
        strategy.setLocale(locale);
        strategy.setCommentable(commentable);
        strategy.setContext(context);
        strategy.setThread(thread);

        boolean isEmpty = thread.getThread().isEmpty();
        boolean acceptNewComments = commentable.getThread().isCommentable()
                        && (enabled == null || enabled == true);

        if (isEmpty && !acceptNewComments) {
            strategy.commentsDisallowed(out);
            return;
        }

        strategy.openContainer(out);

        if (!isEmpty) {
            strategy.openCommentBox(out);
            for (CommentModel comment : thread.getThread()) {
                strategy.comment(comment, out);
            }
            strategy.closeCommentBox(out);
        } else {
            strategy.noCommentHeader(out);
        }

        if (acceptNewComments) {
            // TODO: Also check if the currently logged in user has sufficient rights...
            strategy.commentForm(out);
        }

        strategy.closeContainer(out);
    }

    /**
     * Gets a new {@link CommentRendererStrategy}.
     */
    protected CommentRendererStrategy getCommentRendererStrategy() {
        return applicationContext.getBean(CommentRendererStrategy.class);
    }

}
