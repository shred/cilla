/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2013 Richard "Shred" Körber
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
package org.shredzone.cilla.site.renderer;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import org.shredzone.cilla.web.comment.CommentModel;
import org.shredzone.cilla.web.renderer.AbstractCommentRendererStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Renders a comment area with all comments and a form for submitting new comments.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CommentRendererStrategyImpl extends AbstractCommentRendererStrategy {

    private ResourceBundle bundle;

    @Override
    public void setLocale(Locale locale) {
        super.setLocale(locale);
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    @Override
    public void openContainer(Appendable out) throws IOException {
        out.append("<div>")
           .append("<h1 id=\"comments\">")
           .append(bundle.getString("comment.title"))
           .append("</h1>");
    }

    @Override
    public void noCommentHeader(Appendable out) throws IOException {
        out.append("<p>")
           .append(bundle.getString("comment.nocomments"))
           .append("</p>");
    }

    @Override
    public void comment(CommentModel comment, Appendable out) throws IOException {
        context.setAttribute("comment", comment);
        context.setAttribute("item", commentable);
        context.include("fragment/comment/comment.jspf");
    }

    @Override
    public void commentForm(Appendable out) throws IOException {
        context.include("fragment/comment/form.jspf");
    }

    @Override
    public void closeContainer(Appendable out) throws IOException {
        out.append("</div>");
    }

}
