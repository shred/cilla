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

import org.shredzone.cilla.web.header.DocumentHeaderManager;
import org.shredzone.cilla.web.header.tag.JavaScriptTag;
import org.shredzone.cilla.web.map.MapService;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Inserts all necessary init scripts for maps.
 * <p>
 * Must be invoked before the header fragment is rendered.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Inserts all necessary init scripts for maps.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MapInitTag extends BodyTagSupport {
    private static final long serialVersionUID = 8309606103271334513L;

    @Resource
    private transient MapService mapService;

    @Resource
    private transient DocumentHeaderManager documentHeaderManager;

    private String key;

    @TagParameter
    public void setKey(String key)              { this.key = key; }

    @Override
    public int doEndTag() throws JspException {
        JavaScriptTag js = new JavaScriptTag();
        js.append(mapService.getInitJs(key));
        documentHeaderManager.getDocumentHeader().add(js);
        return EVAL_PAGE;
    }

}
