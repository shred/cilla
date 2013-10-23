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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.commons.taglib.TaglibUtils;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A parameter to the parent tag.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("A parameter to the parent tag.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ParamTag extends BodyTagSupport {
    private static final long serialVersionUID = 3127696613728198638L;

    private @TagParameter(required = true) String name;
    private @TagParameter Object value;

    @Override
    public int doStartTag() throws JspException {
        if (value != null) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        Parameterizable parent = TaglibUtils.findAncestorWithType(this, Parameterizable.class);
        if (parent != null) {
            if (value != null) {
                parent.addParam(name, value);
            } else {
                parent.addParam(name, getBodyContent().getString());
            }
        }
        return EVAL_PAGE;
    }

}
