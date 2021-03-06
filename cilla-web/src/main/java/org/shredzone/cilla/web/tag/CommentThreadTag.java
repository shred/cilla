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
package org.shredzone.cilla.web.tag;

import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.web.comment.CommentThreadModel;
import org.shredzone.cilla.web.comment.CommentThreadService;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Fetches a {@link CommentThreadModel} of a {@link Commentable}.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Fetches a CommentThreadModel for a Commentable")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CommentThreadTag extends BodyTagSupport {
    private static final long serialVersionUID = -7835017044001110609L;

    @Resource
    private transient CommentThreadService commentThreadService;

    private String var;
    private String scope;
    private Commentable item;

    @TagParameter(required = true)
    public void setVar(String var)              { this.var = var; }

    @TagParameter
    public void setScope(String scope)          { this.scope = scope; }

    @TagParameter(required = true)
    public void setItem(Commentable item)       { this.item = item; }

    @Override
    public int doStartTag() throws JspException {
        CommentThreadModel model = commentThreadService.getCommentThread(item);
        TagUtils.setScopedAttribute(pageContext, var, model, scope);
        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
