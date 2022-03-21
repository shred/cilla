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
import java.util.HashMap;
import java.util.Map;

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
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * Creates a link to an internal page.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Creates a link to an internal page.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkTag extends BodyTagSupport implements Parameterizable {
    private static final long serialVersionUID = 5910446039930945197L;

    @Resource
    private transient LinkService linkService;

    private final transient Map<String, Object> parameters = new HashMap<>();

    private String var;
    private String scope;
    private String anchor;
    private String onclick;
    private String styleClass;
    private String style;
    private String title;
    private String rel;
    private String target;
    private boolean absolute;

    private String view;
    private String qualifier;
    private Linkable ref;
    private Commentable commentable;
    private Page page;
    private Category category;
    private Tag tag;
    private User author;
    private Section section;
    private Picture picture;
    private Header header;
    private Page story;

    @TagParameter
    public void setVar(String var)              { this.var = var; }

    @TagParameter
    public void setScope(String scope)          { this.scope = scope; }

    @TagParameter
    public void setAnchor(String anchor)        { this.anchor = anchor; }

    @TagParameter
    public void setOnclick(String onclick)      { this.onclick = onclick; }

    @TagParameter
    public void setStyleClass(String styleClass) { this.styleClass = styleClass; }

    @TagParameter
    public void setStyle(String style)          { this.style = style; }

    @TagParameter
    @Override
    public void setId(String id)                { super.setId(id); }

    @TagParameter
    public void setTitle(String title)          { this.title = title; }

    @TagParameter
    public void setRel(String rel)              { this.rel = rel; }

    @TagParameter
    public void setTarget(String target)        { this.target = target; }

    @TagParameter
    public void setView(String view)            { this.view = view; }

    @TagParameter
    public void setQualifier(String qualifier)  { this.qualifier = qualifier; }

    @TagParameter
    public void setRef(Linkable ref)            { this.ref = ref; }

    @TagParameter
    public void setCommentable(Commentable commentable) { this.commentable = commentable; }

    @TagParameter
    public void setPage(Page page)              { this.page = page; }

    @TagParameter
    public void setCategory(Category category)  { this.category = category; }

    @TagParameter
    public void setTag(Tag tag)                 { this.tag = tag; }

    @TagParameter
    public void setAuthor(User author)          { this.author = author; }

    @TagParameter
    public void setSection(Section section)     { this.section = section; }

    @TagParameter
    public void setPicture(Picture picture)     { this.picture = picture; }

    @TagParameter
    public void setHeader(Header header)        { this.header = header; }

    @TagParameter
    public void setStoryOfPage(Page story)      { this.story = story; }

    @TagParameter
    public void setAbsolute(boolean absolute)   { this.absolute = absolute; }

    @Override
    public void addParam(String key, Object value) {
        parameters.put(key, value);
    }

    @Override
    public int doStartTag() throws JspException {
        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        LinkBuilder lb = linkService.linkTo();

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
        lb.story(story);

        if (absolute) {
            lb.absolute();
        }

        String useTitle = null;
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

        parameters.forEach((key, value) -> {
            if (key.startsWith("#")) {
                lb.param(key.substring(1), value);
            } else {
                lb.query(key, value != null ? value.toString() : "");
            }
        });

        String url = lb.toString();
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

        if (target != null) {
            linktag.append(" target=\"").append(HtmlUtils.htmlEscape(target)).append('"');
        }

        if (rel != null) {
            linktag.append(" rel=\"").append(HtmlUtils.htmlEscape(rel)).append('"');
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
