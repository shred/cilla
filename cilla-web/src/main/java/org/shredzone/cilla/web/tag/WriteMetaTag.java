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
import java.util.SortedSet;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.web.Meta;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Writes all defined meta tags.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.jshred.spring.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Writes all defined meta tags")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WriteMetaTag extends BodyTagSupport {
    private static final long serialVersionUID = 1731947246626975535L;

    @SuppressWarnings("unchecked")
    @Override
    public int doStartTag() throws JspException {
        SortedSet<Meta> metaSet = (SortedSet<Meta>) pageContext.getAttribute(MetaTag.META_ATTRIBUTE_NAME, PageContext.REQUEST_SCOPE);

        if (metaSet != null) {
            try {
                for (Meta meta : metaSet) {
                    pageContext.getOut().println(meta.toString());
                }
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

}
