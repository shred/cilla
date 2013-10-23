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

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.commons.taglib.annotation.Tag;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Body is evaluated if a certain browser is detected.
 *
 * @author Richard "Shred" Körber
 */
@Tag(type = BodyTag.class)
@TagInfo("Body is evaluated if a certain browser is detected.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class IfBrowserTag extends BodyTagSupport {
    private static final long serialVersionUID = 8221367255630620892L;

    private @TagParameter(required = true) String agent;

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        String ua = req.getHeader("User-Agent");
        if (ua != null && Pattern.matches(agent, ua)) {
            return EVAL_BODY_INCLUDE;
        }
        return SKIP_BODY;
    }

}
