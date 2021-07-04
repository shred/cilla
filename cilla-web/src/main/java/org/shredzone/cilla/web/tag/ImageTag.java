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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.web.page.ResourceLockManagerImpl;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

/**
 * Creates an image tag to a medium or picture.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Creates an image tag to a medium or picture.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ImageTag extends BodyTagSupport {
    private static final long serialVersionUID = 5910446039930945197L;

    @Resource
    private transient LinkService linkService;

    @Resource
    private transient ResourceLockManagerImpl unlockService;

    @Value("${resource.deepLinkingProtection}")
    private boolean deepLinkingProtection;

    private String var;
    private String scope;
    private String styleClass;
    private String style;
    private String title;
    private String alt;
    private Picture picture;
    private Header header;
    private Medium medium;
    private String type;
    private Boolean uncropped;
    private Integer width;
    private Integer height;
    private Boolean nosize;

    @TagParameter
    public void setVar(String var)              { this.var = var; }

    @TagParameter
    public void setScope(String scope)          { this.scope = scope; }

    @TagParameter
    public void setStyleClass(String styleClass) { this.styleClass = styleClass; }

    @TagParameter
    public void setStyle(String style)          { this.style = style; }

    @TagParameter
    public void setTitle(String title)          { this.title = title; }

    @TagParameter
    public void setAlt(String alt)              { this.alt = alt; }

    @TagParameter
    public void setPicture(Picture picture)     { this.picture = picture; }

    @TagParameter
    public void setHeader(Header header)        { this.header = header; }

    @TagParameter
    public void setMedium(Medium medium)        { this.medium = medium; }

    @TagParameter
    public void setType(String type)            { this.type = type; }

    @TagParameter
    public void setUncropped(Boolean uncropped) { this.uncropped = uncropped; }

    @TagParameter
    public void setWidth(Integer width)         { this.width = width; }

    @TagParameter
    public void setHeight(Integer height)       { this.height = height; }

    @TagParameter
    public void setNosize(Boolean nosize)       { this.nosize = nosize; }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        String url = null;
        Integer outWidth = null, outHeight = null;

        LinkBuilder lb = linkService.linkTo();

        if (picture != null) {
            lb.view("picture").picture(picture);
            if (deepLinkingProtection) {
                unlockService.unlockStore(request.getSession(), picture);
            }
            if (type != null) {
                lb.param("type", type);
            } else if (nosize == null || !nosize) {
                outWidth = picture.getWidth();
                outHeight = picture.getHeight();
            }
            url = lb.toString();

        } else if (header != null) {
            if (Boolean.TRUE.equals(uncropped)) {
                lb.view("headerUncropped").header(header);
                if (type != null) {
                    lb.param("type", type);
                }
            } else {
                lb.view("headerImage").header(header);
                if (type != null) {
                    lb.param("type", type);
                } else if (nosize == null || !nosize) {
                    outWidth = header.getWidth();
                    outHeight = header.getHeight();
                }
            }
            url = lb.toString();

        } else if (medium != null) {
            lb.view("medium").page(medium.getPage()).param("name", medium.getImage().getName());
            if (type != null) {
                lb.param("type", type);
            }
            url = lb.toString();
        }

        if (width != null) {
            outWidth = width;
        }

        if (height != null) {
            outHeight = height;
        }

        if (url == null) {
            throw new JspException("No image target was set, or image was not found.");
        }

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, url, scope);
            return EVAL_BODY_INCLUDE;
        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();

        StringBuilder imgtag = new StringBuilder();
        imgtag.append("<img src=\"");
        imgtag.append(HtmlUtils.htmlEscape(response.encodeURL(url)));
        imgtag.append('"');

        if (outWidth != null) {
            imgtag.append(" width=\"").append(outWidth).append('"');
        }

        if (outHeight != null) {
            imgtag.append(" height=\"").append(outHeight).append('"');
        }

        if (styleClass != null) {
            imgtag.append(" class=\"").append(HtmlUtils.htmlEscape(styleClass)).append('"');
        }

        if (style != null) {
            imgtag.append(" style=\"").append(HtmlUtils.htmlEscape(style)).append('"');
        }

        if (title != null) {
            imgtag.append(" title=\"").append(HtmlUtils.htmlEscape(title)).append('"');
        }

        if (alt != null) {
            imgtag.append(" alt=\"").append(HtmlUtils.htmlEscape(alt)).append('"');
        }

        imgtag.append(" />");

        try {
            pageContext.getOut().print(imgtag.toString());
        } catch (IOException ex) {
            throw new JspException(ex);
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
