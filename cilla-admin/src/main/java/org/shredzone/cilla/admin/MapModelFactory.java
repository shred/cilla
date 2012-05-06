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

import org.shredzone.cilla.admin.page.PageSelectionObserver;
import org.shredzone.cilla.ws.Geolocated;
import org.shredzone.cilla.ws.page.PageDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Creates {@link EditableMapModel} for a {@link Geolocated} object. Also remembers the
 * last position of the marker, so {@link EditableMapModel} without a valid geolocation
 * show a map around the last location.
 * <p>
 * This component is session scoped!
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class MapModelFactory implements PageSelectionObserver {

    private BigDecimal lastLatitude = null;
    private BigDecimal lastLongitude = null;

    /**
     * Creates an {@link EditableMapModel} for the given {@link Geolocated} object.
     *
     * @param location
     *            {@link Geolocated} to create an {@link EditableMapModel} for
     * @return {@link EditableMapModel} that was created
     */
    public EditableMapModel createMapModel(Geolocated location) {
        return new EditableMapModel(this, location);
    }

    /**
     * Stores the last location that was used, in the user's session.
     */
    public void setLastLocation(BigDecimal lat, BigDecimal lng) {
        this.lastLatitude = lat;
        this.lastLongitude = lng;
    }

    /**
     * Gets the last stored latitude. May be {@code null} if none was set.
     */
    public BigDecimal getLastLatitude() {
        return lastLatitude;
    }

    /**
     * Gets the last stored longitude. May be {@code null} if none was set.
     */
    public BigDecimal getLastLongitude() {
        return lastLongitude;
    }

    @Override
    public void onPageSelected(PageDto selectedPage) {
        // On page change, reset the last coordinates
        lastLatitude = null;
        lastLongitude = null;
    }

}
