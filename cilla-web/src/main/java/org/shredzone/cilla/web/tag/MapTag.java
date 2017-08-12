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

import static org.springframework.web.util.HtmlUtils.htmlEscape;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.core.model.embed.Geolocation;
import org.shredzone.cilla.web.map.MapModel;
import org.shredzone.cilla.web.map.MapService;
import org.shredzone.cilla.web.map.MapType;
import org.shredzone.cilla.web.map.Marker;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Shows a map around a location.
 *
 * @author Richard "Shred" Körber
 */
@org.shredzone.commons.taglib.annotation.Tag(type = BodyTag.class)
@TagInfo("Shows a map around a location.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class MapTag extends BodyTagSupport implements Parameterizable {
    private static final long serialVersionUID = 7313809751943580566L;

    @Resource
    private transient MapService mapService;

    private Geolocation location;
    private boolean satellite;
    private String divId;
    private String style;
    private String styleClass;
    private String var;
    private String scope;

    private MapModel data;

    @TagParameter(required = true)
    public void setLocation(Geolocation location) { this.location = location; }

    @TagParameter
    public void setSatellite(boolean satellite) { this.satellite = satellite; }

    @TagParameter
    public void setDivId(String divId)          { this.divId = divId; }

    @TagParameter
    public void setStyle(String style)          { this.style = style; }

    @TagParameter
    public void setStyleClass(String styleClass) { this.styleClass = styleClass; }

    @TagParameter
    public void setVar(String var)              { this.var = var; }

    @TagParameter
    public void setScope(String scope)          { this.scope = scope; }

    public void addMarker(Marker marker) {
        data.getMarkers().add(marker);
    }

    @Override
    public void addParam(String name, Object value) {
        if ("backgroundColor".equals(name) && value != null) {
            data.setBackgroundColor(value.toString());
        }
    }

    @Override
    public int doStartTag() throws JspException {
        if (divId == null) {
            divId = "map_canvas";
        }

        data = new MapModel();
        data.setLocation(location);
        data.setType(satellite ? MapType.SATELLITE : MapType.ROADMAP);

        return EVAL_BODY_BUFFERED;
    }

    @Override
    public int doEndTag() throws JspException {
        StringBuilder sb = new StringBuilder();
        sb.append("<div id=\"").append(htmlEscape(divId)).append('"');
        if (style != null) {
            sb.append(" style=\"").append(htmlEscape(style)).append('"');
        }
        if (styleClass != null) {
            sb.append(" class=\"").append(htmlEscape(styleClass)).append('"');
        }
        sb.append("></div>\n");

        sb.append("<script type=\"text/javascript\">//<![CDATA[\n");
        sb.append(mapService.build(divId, data));
        sb.append("//]]></script>\n");

        data = null;

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, sb.toString(), scope);
            return EVAL_PAGE;
        }

        try {
            pageContext.getOut().print(sb.toString());
        } catch (IOException ex) {
            throw new JspException(ex);
        }

        return EVAL_PAGE;
    }

}
