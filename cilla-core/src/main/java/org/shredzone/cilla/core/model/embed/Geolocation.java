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
package org.shredzone.cilla.core.model.embed;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * A location somewhere on our blue planet.
 *
 * @author Richard "Shred" Körber
 */
@Embeddable
public class Geolocation implements Serializable {
    private static final long serialVersionUID = -1558116773806332595L;

    private BigDecimal longitude;
    private BigDecimal latitude;
    private BigDecimal altitude;

    /**
     * Longitude. Degrees, positive values are East.
     */
    @Column(precision = 9, scale = 6)
    public BigDecimal getLongitude()            { return longitude; }
    public void setLongitude(BigDecimal longitute) { this.longitude = longitute; }

    /**
     * Latitude. Degrees, positive values are North.
     */
    @Column(precision = 9, scale = 6)
    public BigDecimal getLatitude()             { return latitude;}
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

    /**
     * Altitude above sea level. Meters, negative values are below sea level.
     */
    @Column(precision = 8, scale = 3)
    public BigDecimal getAltitude()             { return altitude; }
    public void setAltitude(BigDecimal altitude) { this.altitude = altitude; }

}
