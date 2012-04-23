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
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.service.link.Linkable;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * Creates a link to an internal page.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.jshred.spring.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Creates a link to an internal page.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkTag extends BodyTagSupport implements Parameterizable {
    private static final long serialVersionUID = 5910446039930945197L;

    private @TagParameter String var;
    private @TagParameter String scope;
    private @TagParameter String anchor;
    private @TagParameter String onclick;
    private @TagParameter String styleClass;
    private @TagParameter String style;
    private @TagParameter String id;
    private @TagParameter String title;

    private @TagParameter String view;
    private @TagParameter String qualifier;
    private @TagParameter Linkable ref;
    private @TagParameter Commentable commentable;
    private @TagParameter Page page;
    private @TagParameter Category category;
    private @TagParameter Tag tag;
    private @TagParameter User author;
    private @TagParameter Section section;
    private @TagParameter Picture picture;
    private @TagParameter Header header;

    private @Resource LinkService linkService;

    private LinkBuilder lb;

    @Override
    public void addParam(String key, Object value) {
        if (key.startsWith("#")) {
            lb.param(key.substring(1), value);
        } else {
            lb.query(key, value != null ? value.toString() : "");
        }
    }

    @Override
    public int doStartTag() throws JspException {
        lb = linkService.linkTo();
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        String url = null;
        String useTitle = null;

        lb.view(view);
        lb.author(author);
        lb.category(category);
        lb.page(page);
        lb.section(section);
        lb.picture(picture);
        lb.tag(tag);
        lb.header(header);
        lb.ref(ref);
        lb.qualifier(qualifier);
        lb.commentable(commentable);

        if (title != null) {
            useTitle = title;
        } else if (page != null) {
            useTitle = page.getTitle();
        } else if (category != null) {
            useTitle = category.getTitle();
        } else if (header != null) {
            useTitle = header.getCaption();
        }

        if (anchor != null) {
            lb.anchor(anchor);
        }

        url = lb.toString();

        if (url == null) {
            throw new JspException("Cannot link to view '" + view + "'");
        }

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, url, scope);
            return EVAL_PAGE;
        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        StringBuilder linktag = new StringBuilder();
        linktag.append("<a href=\"");
        linktag.append(HtmlUtils.htmlEscape(response.encodeURL(url)));
        linktag.append('"');

        if (id != null) {
            linktag.append(" id=\"").append(HtmlUtils.htmlEscape(id)).append('"');
        }

        if (onclick != null) {
            linktag.append(" onclick=\"").append(HtmlUtils.htmlEscape(onclick)).append('"');
        }

        if (styleClass != null) {
            linktag.append(" class=\"").append(HtmlUtils.htmlEscape(styleClass)).append('"');
        }

        if (style != null) {
            linktag.append(" style=\"").append(HtmlUtils.htmlEscape(style)).append('"');
        }

        if (useTitle != null) {
            linktag.append(" title=\"").append(HtmlUtils.htmlEscape(useTitle)).append('"');
        }

        linktag.append('>');

        try {
            pageContext.getOut().print(linktag.toString());

            BodyContent bc = getBodyContent();
            if (bc != null) {
                bc.writeOut(pageContext.getOut());
            }

            pageContext.getOut().print("</a>");
        } catch (IOException ex) {
            throw new JspException(ex);
        }

        return EVAL_PAGE;
    }

}
