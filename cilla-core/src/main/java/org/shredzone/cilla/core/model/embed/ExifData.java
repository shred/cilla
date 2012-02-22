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

import javax.persistence.Embeddable;

/**
 * Contains exif data of a picture. Each entry may be {@code null} if there was no such
 * information found in the exif data.
 *
 * @author Richard "Shred" Körber
 */
@Embeddable
public class ExifData implements Serializable {
    private static final long serialVersionUID = -4983242529766268995L;

    private String cameraModel;
    private String program;
    private String shutter;
    private String aperture;
    private String focalLength;
    private String iso;
    private String whiteBalance;
    private String exposureBias;
    private String flash;
    private String focusMode;
    private String meteringMode;

    /**
     * Camera brand and make.
     */
    public String getCameraModel()              { return cameraModel; }
    public void setCameraModel(String cameraModel) { this.cameraModel = cameraModel; }

    /**
     * Program the picture was taken in.
     */
    public String getProgram()                  { return program; }
    public void setProgram(String program)      { this.program = program; }

    /**
     * Shutter speed.
     */
    public String getShutter()                  { return shutter; }
    public void setShutter(String shutter)      { this.shutter = shutter; }

    /**
     * Aperture.
     */
    public String getAperture()                 { return aperture; }
    public void setAperture(String aperture)    { this.aperture = aperture; }

    /**
     * Focal length.
     */
    public String getFocalLength()              { return focalLength; }
    public void setFocalLength(String focalLength) { this.focalLength = focalLength; }

    /**
     * ISO value.
     */
    public String getIso()                      { return iso; }
    public void setIso(String iso)              { this.iso = iso; }

    /**
     * White balance mode.
     */
    public String getWhiteBalance()             { return whiteBalance; }
    public void setWhiteBalance(String whiteBalance) { this.whiteBalance = whiteBalance; }

    /**
     * Exposure bias.
     */
    public String getExposureBias()             { return exposureBias; }
    public void setExposureBias(String exposureBias) { this.exposureBias = exposureBias; }

    /**
     * Flash mode.
     */
    public String getFlash()                    { return flash; }
    public void setFlash(String flash)          { this.flash = flash; }

    /**
     * Focus mode.
     */
    public String getFocusMode()                { return focusMode; }
    public void setFocusMode(String focusMode)  { this.focusMode = focusMode; }

    /**
     * Metering mode.
     */
    public String getMeteringMode()             { return meteringMode; }
    public void setMeteringMode(String meteringMode) { this.meteringMode = meteringMode; }

}
