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
package org.shredzone.cilla.admin;

import java.math.BigDecimal;

import org.primefaces.event.map.MarkerDragEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;

// TODO: incomplete

/**
 * A model providing a map where a position can be marked upon.
 *
 * @author Richard "Shred" Körber
 */
public class EditableMapModel extends DefaultMapModel {
    private static final long serialVersionUID = 8478083672796714573L;

    private Marker marker;

    public EditableMapModel() {
        this(0d, 0d);
    }

    public EditableMapModel(BigDecimal lat, BigDecimal lng) {
        this(
            (lat != null ? lat.doubleValue() : 0d),
            (lng != null ? lng.doubleValue() : 0d)
        );
    }

    public EditableMapModel(double lat, double lng) {
        marker = new Marker(new LatLng(lat, lng), "here"); // TODO: i18n
        marker.setDraggable(true);
        addOverlay(marker);
    }

    public void onMarkerDrag(MarkerDragEvent event) {
        if (event.getMarker() == marker) {
            // TODO: Store the new marker position
        }
    }

    public void setLatitude(double lat) {
        LatLng newPos = new LatLng(lat, marker.getLatlng().getLng());
        marker.setLatlng(newPos);
    }

    public void setLongitude(double lng) {
        LatLng newPos = new LatLng(marker.getLatlng().getLat(), lng);
        marker.setLatlng(newPos);
    }

    public double getLatitude() {
        return marker.getLatlng().getLat();
    }

    public double getLongitude() {
        return marker.getLatlng().getLng();
    }

    public String getCenter() {
        return marker.getLatlng().toString();
    }

}
