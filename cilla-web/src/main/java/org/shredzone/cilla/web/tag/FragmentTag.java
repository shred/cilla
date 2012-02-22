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

import org.shredzone.jshred.spring.taglib.annotation.Tag;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A fragment to be used in other tags.
 *
 * @author Richard "Shred" Körber
 */
@Tag(type = BodyTag.class)
@TagInfo("A fragment to be used in other tags.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FragmentTag extends BodyTagSupport {
    private static final long serialVersionUID = 1795786153840117251L;
    public static final String FRAGMENT_ATTRIBUTE = "cillaFragment";

    private @TagParameter(required = true) String type;

    @Override
    public int doStartTag() throws JspException {
        String requestedFragment = (String) pageContext.getAttribute(FRAGMENT_ATTRIBUTE);

        if (type.equals(requestedFragment)) {
            return EVAL_BODY_INCLUDE;
        } else {
            return SKIP_BODY;
        }
    }

}
