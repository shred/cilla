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

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.web.Meta;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Adds a meta tag.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.jshred.spring.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Adds a meta tag.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MetaTag extends BodyTagSupport {
    private static final long serialVersionUID = 1155264881573999741L;

    public static final String META_ATTRIBUTE_NAME = "meta";

    private @TagParameter(required = true) String name;
    private @TagParameter String content;
    private @TagParameter String scheme;
    private @TagParameter boolean replace = false;

    @Override
    public int doStartTag() throws JspException {
        if (content != null) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public int doEndTag() throws JspException {
        String value = content;

        if (value == null) {
            value = getBodyContent().getString();
        }

        SortedSet<Meta> metaSet = (SortedSet<Meta>) pageContext.getAttribute(META_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);
        if (metaSet == null) {
            metaSet = new TreeSet<Meta>();
            pageContext.setAttribute(META_ATTRIBUTE_NAME, metaSet, PageContext.REQUEST_SCOPE);
        }

        Meta meta = new Meta(name, value, scheme);

        if (!replace && metaSet.contains(meta)) {
            throw new JspException("Meta " + name + " is already set");
        }

        metaSet.add(meta);

        return EVAL_PAGE;
    }

}
