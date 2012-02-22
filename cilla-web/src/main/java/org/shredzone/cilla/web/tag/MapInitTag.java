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
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.web.map.MapService;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.jshred.spring.taglib.annotation.TagInfo;
import org.shredzone.jshred.spring.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Inserts all necessary init scripts for maps.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.jshred.spring.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Inserts all necessary init scripts for maps.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MapInitTag extends BodyTagSupport {
    private static final long serialVersionUID = 8309606103271334513L;

    private @TagParameter String var;
    private @TagParameter String scope;

    private @Resource MapService mapService;

    @Override
    public int doEndTag() throws JspException {
        StringBuilder sb = new StringBuilder();
        sb.append(mapService.getInitJs());

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, sb.toString(), scope);
            return EVAL_PAGE;
        }

        try {
            pageContext.getOut().println(sb.toString());
        } catch (IOException ex) {
            throw new JspException(ex);
        }

        return EVAL_PAGE;
    }

}
