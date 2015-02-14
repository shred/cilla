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

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.TagSupport;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.service.link.Linkable;
import org.shredzone.cilla.web.FeedType;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * Creates a link to a feed.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = IterationTag.class, bodycontent = "no")
@TagInfo("Creates a link to a feed.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FeedTag extends TagSupport {
    private static final long serialVersionUID = -8820686144322884700L;

    private @Resource LinkService linkService;

    private String type;
    private String var;
    private String scope;
    private String title;
    private Linkable ref;
    private Category category;
    private Tag tag;
    private User author;

    @TagParameter(required = true)
    public void setType(String type)            { this.type = type; }

    @TagParameter
    public void setVar(String var)              { this.var = var; }

    @TagParameter
    public void setScope(String scope)          { this.scope = scope; }

    @TagParameter
    public void setTitle(String title)          { this.title = title; }

    @TagParameter
    public void setRef(Linkable ref)            { this.ref = ref; }

    @TagParameter
    public void setCategory(Category category)  { this.category = category; }

    @TagParameter
    public void setTag(Tag tag)                 { this.tag = tag; }

    @TagParameter
    public void setAuthor(User author)          { this.author = author; }

    @Override
    public int doEndTag() throws JspException {

        FeedType feedType = FeedType.valueOf(type);

        LinkBuilder lb = linkService.linkTo();
        lb.param("feed", feedType.getSuffix());

        if (category != null) {
            lb.category(category);

        } else if (tag != null) {
            lb.tag(tag);

        } else if (author != null) {
            lb.author(author);

        } else if (ref != null) {
            lb.ref(ref);
        }

        String url = lb.toString();

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, url, scope);
            return EVAL_PAGE;
        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        StringBuilder linktag = new StringBuilder();
        linktag.append("<link rel=\"alternate\" type=\"").append(feedType.getContentType()).append('"');
        if (title != null) {
            linktag.append(" title=\"");
            linktag.append(HtmlUtils.htmlEscape(title));
            linktag.append('"');
        }
        linktag.append(" href=\"");
        linktag.append(HtmlUtils.htmlEscape(response.encodeURL(url)));
        linktag.append("\" />");

        try {
            pageContext.getOut().print(linktag.toString());
        } catch (IOException ex) {
            throw new JspException(ex);
        }

        return EVAL_PAGE;
    }

}
