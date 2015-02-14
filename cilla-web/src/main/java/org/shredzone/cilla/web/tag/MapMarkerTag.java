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

import org.shredzone.cilla.core.model.embed.Geolocation;
import org.shredzone.cilla.web.map.Marker;
import org.shredzone.commons.taglib.TaglibUtils;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A marker for a Map tag.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("A marker for a Map tag.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MapMarkerTag extends BodyTagSupport {
    private static final long serialVersionUID = 3396938844924733218L;

    private Geolocation location;
    private String text;
    private String link;

    @TagParameter(required = true)
    public void setLocation(Geolocation location) { this.location = location; }

    @TagParameter
    public void setText(String text)            { this.text = text; }

    @TagParameter
    public void setLink(String link)            { this.link = link; }

    @Override
    public int doStartTag() throws JspException {
        if (text != null) {
            return SKIP_BODY;
        } else {
            return EVAL_BODY_BUFFERED;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        MapTag parent = TaglibUtils.findAncestorWithType(this, MapTag.class);
        if (parent == null) {
            throw new JspException("<cilla:mapMarker> without parent <cilla:map>");
        }

        if (text == null) {
            text = getBodyContent().getString();
        }

        parent.addMarker(new Marker(location, text, link));

        return EVAL_PAGE;
    }

}
