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

import org.shredzone.cilla.web.header.DocumentHeader;
import org.shredzone.cilla.web.header.DocumentHeaderManager;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Adds a meta tag.
 * <p>
 * Must be invoked before the header's meta fragment is rendered.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.jshred.spring.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Adds a meta tag.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MetaTag extends BodyTagSupport {
    private static final long serialVersionUID = 1155264881573999741L;

    private @TagParameter(required = true) String name;
    private @TagParameter String content;
    private @TagParameter String scheme;
    private @TagParameter boolean replace = false;

    private @Resource DocumentHeaderManager documentHeaderManager;

    @Override
    public int doStartTag() throws JspException {
        if (content != null) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        String value = content;
        if (value == null) {
            value = getBodyContent().getString();
        }

        org.shredzone.cilla.web.header.tag.MetaTag meta =
                        new org.shredzone.cilla.web.header.tag.MetaTag(name, value, scheme);

        DocumentHeader header = documentHeaderManager.getDocumentHeader();
        if (!replace && header.contains(meta)) {
            throw new JspException("Meta " + name + " is already set");
        }
        header.add(meta);

        return EVAL_PAGE;
    }

}
