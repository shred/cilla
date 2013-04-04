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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.shredzone.cilla.core.model.embed.Geolocation;

/**
 * A model containing all the data to be rendered by the {@link MapService}.
 *
 * @author Richard "Shred" Körber
 */
public class MapModel implements Serializable {
    private static final long serialVersionUID = 8118613426335207918L;

    private MapType type = MapType.ROADMAP;
    private Geolocation location;
    private int zoom = 17;
    private String backgroundColor = null;
    private List<Marker> markers = new ArrayList<>();

    /**
     * The desired {@link MapType}. If the service does not support this type, it should
     * fall back to a supported type that gives similar results.
     */
    public MapType getType()                    { return type; }
    public void setType(MapType type)           { this.type = type; }

    /**
     * The location to be shown in the center of the map.
     */
    public Geolocation getLocation()            { return location; }
    public void setLocation(Geolocation location) { this.location = location; }

    /**
     * Zoom factor. Defaults to 17.
     */
    public int getZoom()                        { return zoom; }
    public void setZoom(int zoom)               { this.zoom = zoom; }

    /**
     * The background color. It should be used for tiles that are being loaded, or control
     * elements shown outside of the map.
     */
    public String getBackgroundColor()          { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }

    /**
     * A list of {@link Marker} to be shown in the map.
     */
    public List<Marker> getMarkers()            { return markers; }
    public void setMarkers(List<Marker> markers) { this.markers = markers; }

}
