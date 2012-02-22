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

import org.shredzone.cilla.core.model.embed.Geolocation;

/**
 * A marker. It marks a location on the map, optionally with a text and a link. A marker
 * is immutable.
 *
 * @author Richard "Shred" Körber
 */
public class Marker implements Serializable {
    private static final long serialVersionUID = -731967841417686303L;

    private final Geolocation location;
    private final String text;
    private final String link;

    /**
     * Creates a new marker.
     *
     * @param location
     *            {@link Geolocation}
     * @param text
     *            Label
     */
    public Marker(Geolocation location, String text) {
        this(location, text, null);
    }

    /**
     * Creates a new marker.
     *
     * @param location
     *            {@link Geolocation}
     * @param text
     *            Label
     * @param link
     *            Link, or {@code null} if the marker has no link
     */
    public Marker(Geolocation location, String text, String link) {
        this.location = location;
        this.text = text;
        this.link = link;
    }

    /**
     * Link to go to when the marker is clicked.
     */
    public String getLink()             { return link; }

    /**
     * Location of the marker.
     */
    public Geolocation getLocation()    { return location; }

    /**
     * Caption of the marker.
     */
    public String getText()             { return text; }

}
