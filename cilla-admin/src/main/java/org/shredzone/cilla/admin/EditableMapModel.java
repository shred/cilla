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
import java.math.MathContext;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Marker;
import org.shredzone.cilla.ws.Geolocated;

/**
 * A model providing a map where a position can be marked upon.
 *
 * @author Richard "Shred" Körber
 */
public class EditableMapModel extends DefaultMapModel {
    private static final long serialVersionUID = 8478083672796714573L;

    private static final MathContext CONTEXT = new MathContext(9);

    private final MapModelFactory factory;
    private final Geolocated model;
    private final Marker marker;
    private int initZoom = 15;

    /**
     * Creates a new {@link EditableMapModel}.
     *
     * @param factory
     *            {@link MapModelFactory} that created this model. It is used for getting
     *            and setting the last position.
     * @param model
     *            {@link Geolocated} object to be edited
     */
    public EditableMapModel(MapModelFactory factory, Geolocated model) {
        this.factory = factory;
        this.model = model;

        double lat, lng;

        if (model.getLatitude() != null && model.getLongitude() != null) {
            lat = model.getLatitude().doubleValue();
            lng = model.getLongitude().doubleValue();
        } else if (factory.getLastLatitude() != null && factory.getLastLongitude() != null) {
            lat = factory.getLastLatitude().doubleValue();
            lng = factory.getLastLongitude().doubleValue();
        } else {
            lat = 0d;
            lng = 0d;
            initZoom = 1;
        }

        String label = getResourceBundle().getString("mapedit.marker");

        marker = new Marker(new LatLng(lat, lng), label);
        marker.setDraggable(true);
        addOverlay(marker);
    }

    /**
     * Commits the position, writing it to the {@link Geolocated}.
     */
    public void commit() {
        BigDecimal lat = new BigDecimal(Double.toString(marker.getLatlng().getLat()), CONTEXT);
        BigDecimal lng = new BigDecimal(Double.toString(marker.getLatlng().getLng()), CONTEXT);

        model.setLatitude(lat);
        model.setLongitude(lng);
        model.setAltitude(null);

        factory.setLastLocation(lat, lng);
    }

    /**
     * Clears the position at the {@link Geolocated}.
     */
    public void clear() {
        model.setLatitude(null);
        model.setLongitude(null);
        model.setAltitude(null);
    }

    /**
     * Gets the current latitude of the marker.
     */
    public double getLatitude() {
        return marker.getLatlng().getLat();
    }

    /**
     * Gets the current longitude of the marker.
     */
    public double getLongitude() {
        return marker.getLatlng().getLng();
    }

    /**
     * Gets the center position of the map to be used. This is usually the marker's
     * position, or the last known marker position.
     */
    public String getCenter() {
        return getLatitude() + ", " + getLongitude();
    }

    /**
     * Gets the initial zoom level of the map.
     */
    public int getZoom() {
        return initZoom;
    }

    /**
     * Invoked when an empty point was selected in the map. The marker is placed to the
     * position of the click, and the map is zoomed in.
     *
     * @param event
     *            {@link PointSelectEvent} containing the click coordinate
     */
    public void onPointSelect(PointSelectEvent event) {
        marker.setLatlng(event.getLatLng());

        // Zoom in...
        if (initZoom < 5) {
            initZoom = 5;
        } else if (initZoom < 17) {
            initZoom += 3;
        }
    }

    /**
     * Returns a {@link ResourceBundle} to be used for i18n. The locale used depends on
     * the locale settings of the visiting user.
     */
    private ResourceBundle getResourceBundle() {
        FacesContext ctx = FacesContext.getCurrentInstance();
        Locale loc = ctx.getViewRoot().getLocale();
        return ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), loc);
    }

}
