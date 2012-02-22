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
package org.shredzone.cilla.web.map;

import org.shredzone.cilla.core.model.embed.Geolocation;

/**
 * A {@link MapService} implementation that uses Google Maps.
 *
 * @author Richard "Shred" Körber
 */
public class GoogleMapServiceImpl implements MapService {

    @Override
    public String getInitJs() {
        StringBuilder sb = new StringBuilder();

        sb.append("<script type=\"text/javascript\">//<![CDATA[\n");
        sb.append("var maploader = {");
        sb.append("url:'http://maps.google.com/maps/api/js?sensor=false&callback=maploader.start',");
        sb.append("setup:false,");
        sb.append("callback:null,");
        sb.append("show:function(callback){if(this.setup){callback()}else{this.callback=callback;this.init()}},");
        sb.append("start:function(){this.callback()},");
        sb.append("init:function(){");
        sb.append("var s=document.createElement('script');");
        sb.append("s.type='text/javascript';");
        sb.append("s.src=this.url;");
        sb.append("document.body.appendChild(s);");
        sb.append("this.setup=true;");
        sb.append("}");
        sb.append("}\n");
        sb.append("//]]></script>\n");

        return sb.toString();
    }

    @Override
    public String build(String divId, MapModel data) {
        StringBuilder sb = new StringBuilder();

        sb.append("maploader.show(function(){");
        sb.append("var map=new google.maps.Map(document.getElementById('");
        sb.append(escapeJs(divId));
        sb.append("'),{");
        sb.append("zoom:").append(data.getZoom()).append(',');

        if (data.getBackgroundColor() != null) {
            sb.append("backgroundColor:'").append(escapeJs(data.getBackgroundColor())).append("',");
        }

        if (data.getLocation() != null) {
            sb.append("center:");
            appendLatLong(sb, data.getLocation());
            sb.append(',');
        }

        appendMapTypeId(sb, data.getType());
        sb.append("});");

        for (Marker marker : data.getMarkers()) {
            appendMarker(sb, marker);
        }

        sb.append(";});");

        return sb.toString();
    }

    /**
     * Appends a {@link MapType} to the {@link StringBuilder}.
     *
     * @param sb
     *            {@link StringBuilder} to append to
     * @param type
     *            {@link MapType} to append
     */
    private void appendMapTypeId(StringBuilder sb, MapType type) {
        if (type == MapType.ROADMAP) {
            sb.append("mapTypeId:google.maps.MapTypeId.ROADMAP");
        } else if (type == MapType.SATELLITE) {
            sb.append("mapTypeId:google.maps.MapTypeId.HYBRID");
        } else {
            throw new IllegalArgumentException("Unknown MapType " + type.toString());
        }
    }

    /**
     * Appends a {@link Marker} to the {@link StringBuilder}.
     *
     * @param sb
     *            {@link StringBuilder} to append to
     * @param marker
     *            {@link Marker} to append
     */
    private void appendMarker(StringBuilder sb, Marker marker) {
        sb.append("new google.maps.Marker({position:");
        appendLatLong(sb, marker.getLocation());
        sb.append(",map:map");
        if (marker.getText() != null) {
            sb.append(",title:'").append(escapeJs(marker.getText())).append('\'');
        }
        sb.append("});");
    }

    /**
     * Append a {@link Geolocation} to the {@link StringBuffer}.
     *
     * @param sb
     *            {@link StringBuilder} to append to
     * @param location
     *            {@link Geolocation} to append
     */
    private void appendLatLong(StringBuilder sb, Geolocation location) {
        sb.append("new google.maps.LatLng(");
        sb.append(location.getLatitude()).append(',');
        sb.append(location.getLongitude()).append(')');
    }

    /**
     * Escapes a JavaScript string.
     *
     * @param str
     *            String to be escaped
     * @return escaped string
     */
    private static String escapeJs(String str) {
        return str.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}
