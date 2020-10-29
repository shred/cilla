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
package org.shredzone.cilla.service.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;

import com.drew.imaging.PhotographicConversions;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.lang.Rational;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.exif.makernotes.CanonMakernoteDirectory;
import com.drew.metadata.exif.makernotes.CasioType2MakernoteDirectory;
import com.drew.metadata.exif.makernotes.PentaxMakernoteDirectory;
import org.shredzone.cilla.core.model.embed.ExifData;
import org.shredzone.cilla.core.model.embed.Geolocation;
import org.slf4j.LoggerFactory;

/**
 * Analyzes the EXIF and GPS information of a JPEG image. It's a wrapper around the
 * Metadata Extractor of Drew Noakes.
 *
 * @author Richard "Shred" Körber
 * @see <a href="http://drewnoakes.com/code/exif/">Metadata Extractor</a>
 */
public class ExifAnalyzer {
    private final Metadata metadata;

    /**
     * Creates a new {@link ExifAnalyzer} for the given {@link Metadata}.
     * <p>
     * Private, use the factory classes instead.
     *
     * @param metadata
     *            {@link Metadata} to analyze
     */
    private ExifAnalyzer(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * Creates a new {@link ExifAnalyzer} for the given JPEG file.
     *
     * @param file
     *            JPEG file to analyze
     * @return {@link ExifAnalyzer} or {@code null} if it is no valid JPEG image.
     */
    public static ExifAnalyzer create(File file) throws IOException {
        try {
            return new ExifAnalyzer(JpegMetadataReader.readMetadata(file));
        } catch (JpegProcessingException ex) {
            LoggerFactory.getLogger(ExifAnalyzer.class)
                    .debug("Could not analyze file " + file.getName(), ex);
            return null;
        }
    }

    /**
     * Creates a new {@link ExifAnalyzer} for the given JPEG input stream.
     *
     * @param in
     *            JPEG input stream to analyze
     * @return {@link ExifAnalyzer} or {@code null} if it is no valid JPEG image.
     */
    public static ExifAnalyzer create(InputStream in) throws IOException {
        try {
            return new ExifAnalyzer(JpegMetadataReader.readMetadata(in));
        } catch (JpegProcessingException ex) {
            LoggerFactory.getLogger(ExifAnalyzer.class)
                    .debug("Could not analyze input stream", ex);
            return null;
        }
    }

    /**
     * Gets the Metadata containing the EXIF information.
     *
     * @return Metadata
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Gets the {@link ExifData} of the picture taken.
     *
     * @return {@link ExifData}, never {@code null}, but it could be empty.
     */
    public ExifData getExifData() {
        ExifData exif = new ExifData();

        exif.setCameraModel(getCameraModel());
        exif.setAperture(getAperture());
        exif.setShutter(getShutter());
        exif.setIso(getIso());
        exif.setExposureBias(getExposureBias());
        exif.setFocalLength(getFocalLength());
        exif.setFlash(getFlash());
        exif.setWhiteBalance(getWhiteBalance());
        exif.setMeteringMode(getMeteringMode());
        exif.setFocusMode(getFocusMode());
        exif.setProgram(getProgram());

        return exif;
    }

    /**
     * Gets the {@link Geolocation} where the picture was taken.
     *
     * @return {@link Geolocation}, never {@code null}, but it could be empty.
     */
    public Geolocation getGeolocation() {
        Geolocation location = new Geolocation();

        Optional<BigDecimal> longitude = readAngle(GpsDirectory.class, GpsDirectory.TAG_LONGITUDE);
        if (longitude.isPresent()) {
            Optional<String> longRef = readString(GpsDirectory.class, GpsDirectory.TAG_LONGITUDE_REF);
            if ("W".equals(longRef.orElse(null))) {
                location.setLongitude(longitude.get().negate());
            } else {
                location.setLongitude(longitude.get());
            }
        }

        Optional<BigDecimal> latitude = readAngle(GpsDirectory.class, GpsDirectory.TAG_LATITUDE);
        if (latitude.isPresent()) {
            Optional<String> latRef = readString(GpsDirectory.class, GpsDirectory.TAG_LATITUDE_REF);
            if ("S".equals(latRef.orElse(null))) {
                location.setLatitude(latitude.get().negate());
            } else {
                location.setLatitude(latitude.get());
            }
        }

        Optional<Rational> altitude = readRational(GpsDirectory.class, GpsDirectory.TAG_ALTITUDE);
        if (altitude.isPresent()) {
            BigDecimal altDec = BigDecimal.valueOf(altitude.get().doubleValue()).setScale(3, RoundingMode.HALF_DOWN);

            Optional<String> altRef = readString(GpsDirectory.class, GpsDirectory.TAG_ALTITUDE_REF);
            if ("1".equals(altRef.orElse(null))) {
                location.setAltitude(altDec.negate());
            } else {
                location.setAltitude(altDec);
            }
        }

        return location;
    }

    /**
     * Gets the date and time when the picture was taken according to the EXIF data.
     *
     * @param tz
     *         The camera's TimeZone
     * @return Date and time, or {@code null} if the information could not be retrieved
     */
    public Date getDateTime(TimeZone tz) {
        // JDK9: use Optional.or()
        Optional<Date> date = readDate(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL, tz);

        if (!date.isPresent()) {
            date = readDate(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED, tz);
        }

        if (!date.isPresent()) {
            date = readDate(ExifIFD0Directory.class, ExifIFD0Directory.TAG_DATETIME, tz);
        }

        return date.orElse(null);
    }

    /**
     * Gets the Camera Model.
     *
     * @return Camera Model string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getCameraModel() {
        // JDK9: use Optional.or()
        Optional<String> model = readString(ExifIFD0Directory.class, ExifIFD0Directory.TAG_MODEL);
        if (model.isPresent()) {
            return model.get();
        }

        model = readString(ExifIFD0Directory.class, ExifIFD0Directory.TAG_MAKE);
        if (model.isPresent()) {
            return model.get();
        }

        return null;
    }

    /**
     * Gets the Aperture in F-Stops of the photo taken. Format is "f/6.0".
     *
     * @return Aperture string, or {@code null} if the information could not be retrieved
     */
    public String getAperture() {
        Optional<Rational> aperture = readRational(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_APERTURE);

        if (aperture.isPresent()) {
            double fstop = PhotographicConversions.apertureToFStop(aperture.get().doubleValue());
            return String.format(Locale.ENGLISH, "f/%.1f", fstop);
        }

        return null;
    }

    /**
     * Gets the Shutter Speed of the photo taken. Format is either "1/150 s" or "15.0 s".
     *
     * @return Shutter Speed string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getShutter() {
        Optional<Rational> shutter = readRational(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_SHUTTER_SPEED);

        if (shutter.isPresent()) {
            double speed = PhotographicConversions.shutterSpeedToExposureTime(shutter.get().doubleValue());
            if (speed <= .25d) {
                return String.format(Locale.ENGLISH, "1/%.0f s", 1 / speed);
            } else {
                return String.format(Locale.ENGLISH, "%.1f s", speed);
            }
        }

        return null;
    }

    /**
     * Gets the ISO value of the photo taken. Format: "100".
     *
     * @return ISO string, or {@code null} if the information could not be retrieved
     */
    public String getIso() {
        Optional<Integer> iso = readInteger(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_ISO_EQUIVALENT);
        if (iso.isPresent()) {
            return String.format(Locale.ENGLISH, "%d", iso.get());
        }

        return null;

    }

    /**
     * Gets the Exposure Bias of the photo taken. Format: "+0.7 EV" (zero is "+0.0 EV").
     *
     * @return Exposure Bias string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getExposureBias() {
        Optional<Rational> bias = readRational(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_EXPOSURE_BIAS);
        if (bias.isPresent()) {
            return String.format(Locale.ENGLISH, "%+.1f EV", bias.get().doubleValue());
        }

        return null;
    }

    /**
     * Gets the Focal Length of the photo taken. Format: "123.0 mm". If there is also a 35
     * mm film equivalent focal length, the format is "123.0 mm (= 196.8 mm)".
     *
     * @return Focal Length string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getFocalLength() {
        Optional<Rational> focal = readRational(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_FOCAL_LENGTH);

        if (focal.isPresent()) {
            String result = String.format(Locale.ENGLISH, "%.0f mm", focal.get().doubleValue());

            Optional<Integer> equiv = readInteger(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_35MM_FILM_EQUIV_FOCAL_LENGTH);
            if (equiv.isPresent() && focal.get().intValue() != equiv.get().intValue()) {
                result += String.format(Locale.ENGLISH, " (= %d mm)", equiv.get());
            }

            return result;
        }

        return null;
    }

    /**
     * Reads the Flash Mode that was set on the camera for this photo. Flash Mode may
     * consist of several information separated by comma (',').
     *
     * @return Flash Mode string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getFlash() {
        Optional<Integer> code;

        code = readInteger(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_FLASH);
        if (code.isPresent()) {
            int value = code.get();
            if (value == 0) {
                return "no flash";
            }

            StringBuilder sb = new StringBuilder();
            switch (value & 0x18) {
                case 0x08: sb.append("on"); break;
                case 0x10: sb.append("off"); break;
                case 0x18: sb.append("auto"); break;
            }

            if ((value & 0x01) != 0) {
                sb.append(",fired");
            }

            if ((value & 0x06) == 0x06) {
                sb.append(",return detected");
            }

            // Too much information for a mere gallery
            // if ((value & 0x06) == 0x04) {
            // sb.append(",return not detected");
            // }

            // Too much information for a mere gallery
            // if ((value & 0x20) != 0) {
            // sb.append(",no flash function");
            // }

            if ((value & 0x40) != 0) {
                sb.append(",red eye reduction");
            }

            if (sb.charAt(0) == ',') {
                sb.deleteCharAt(0);
            }

            return sb.toString();
        }

        return null;
    }

    /**
     * Reads the White Balance Mode that was set on the camera for this photo.
     *
     * @return White Balance Mode string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getWhiteBalance() {
        Optional<Integer> code;

        code = readInteger(CanonMakernoteDirectory.class, CanonMakernoteDirectory.FocalLength.TAG_WHITE_BALANCE);
        if (code.isPresent()) {
            switch (code.get()) {
                case 0: return "auto";
                case 1: return "daylight";
                case 2: return "cloudy";
                case 3: return "tungsten";
                case 4: return "fluorescent";
                case 5: return "flash";
                case 6: return "manual";
            }
        }

        code = readInteger(CasioType2MakernoteDirectory.class, CasioType2MakernoteDirectory.TAG_WHITE_BALANCE_1);
        if (code.isPresent()) {
            switch (code.get()) {
                case 0: return "auto";
                case 1: return "daylight";
                case 2: return "cloudy";
                case 3: return "tungsten";
                case 4: return "fluorescent";
                case 5: return "manual";
            }
        }

        code = readInteger(PentaxMakernoteDirectory.class, PentaxMakernoteDirectory.TAG_WHITE_BALANCE);
        if (code.isPresent()) {
            switch (code.get()) {
                case 0: return "auto";
                case 1: return "daylight";
                case 2: return "cloudy";
                case 3: return "tungsten";
                case 4: return "fluorescent";
                case 5: return "manual";
            }
        }

        // Other makes are undocumented and thus not evaluated

        code = readInteger(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_WHITE_BALANCE);
        if (code.isPresent()) {
            switch (code.get()) {
                case 1: return "daylight";
                case 2: return "fluorescent";
                case 3: return "tungsten";
                case 10: return "flash";
            }
        }

        code = readInteger(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_WHITE_BALANCE_MODE);
        if (code.isPresent()) {
            switch (code.get()) {
                case 0: return "auto";
                case 1: return "manual";
            }
        }

        return null;
    }

    /**
     * Reads the Metering Mode that was set on the camera for this photo.
     *
     * @return Metering Mode string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getMeteringMode() {
        Optional<Integer> code;

        code = readInteger(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_METERING_MODE);
        if (code.isPresent()) {
            switch (code.get()) {
                case 1: return "average";
                case 2: return "center weighted average";
                case 3: return "spot";
                case 4: return "multi spot";
                case 5: return "multi segment";
                case 6: return "partial";
            }
        }

        return null;
    }

    /**
     * Reads the Focus Mode that was set on the camera for this photo.
     *
     * @return Focus Mode string, or {@code null} if the information could not be
     *         retrieved
     */
    public String getFocusMode() {
        Optional<Integer> code;

        code = readInteger(CanonMakernoteDirectory.class, CanonMakernoteDirectory.CameraSettings.TAG_FOCUS_MODE_1);
        if (code.isPresent()) {
            switch (code.get()) {
                case  0: return "one shot";
                case  1: return "ai servo";
                case  2: return "ai focus";
                case  3: return "manual";
                case  4: return "single";
                case  5: return "continuous";
                case  6: return "manual"; //NOSONAR
                case 16: return "pan";
            }
        }

        code = readInteger(CasioType2MakernoteDirectory.class, CasioType2MakernoteDirectory.TAG_FOCUS_MODE_2);
        if (code.isPresent()) {
            switch (code.get()) {
                case 0: return "manual";
                case 1: return "focus lock";
                case 2: return "macro";
                case 3: return "single-area";
                case 5: return "infinity";
                case 6: return "multi-area";
                case 8: return "super macro";
            }
        }

        return null;
    }

    /**
     * Reads the Program that was set on the camera for this photo.
     *
     * @return Program string, or {@code null} if the information could not be retrieved
     */
    public String getProgram() {
        Optional<Integer> code;

        code = readInteger(CanonMakernoteDirectory.class, CanonMakernoteDirectory.CameraSettings.TAG_EXPOSURE_MODE);
        if (code.isPresent()) {
            switch (code.get()) {
                case 1: return "program";
                case 2: return "shutter speed priority";
                case 3: return "aperture priority";
                case 4: return "manual";
                case 5: return "depth-of-field";
                case 6: return "m-dep";
                case 7: return "bulb";
            }
        }

        code = readInteger(CanonMakernoteDirectory.class, CanonMakernoteDirectory.CameraSettings.TAG_EASY_SHOOTING_MODE);
        if (code.isPresent()) {
            switch (code.get()) {
                case   0: return "auto";
                case   1: return "easy";
                case   2: return "landscape";
                case   3: return "fast shutter";
                case   4: return "slow shutter";
                case   5: return "night";
                case   6: return "gray scale";
                case   7: return "sepia";
                case   8: return "portrait";
                case   9: return "sports";
                case  10: return "macro";
                case  11: return "black and white";
                case  13: return "vivid";
                case  14: return "neutral";
                case  15: return "flash off";
                case  16: return "long shutter";
                case  17: return "super macro";
                case  18: return "foliage";
                case  19: return "indoor";
                case  20: return "fireworks";
                case  21: return "beach";
                case  22: return "underwater";
                case  23: return "snow";
                case  24: return "kids and pets";
                case  25: return "night snapshot";
                case  26: return "digital macro";
                case  27: return "my colors";
                case  28: return "still image";
                case  30: return "color accent";
                case  31: return "color swap";
                case  32: return "aquarium";
                case  33: return "iso 3200";
                case  38: return "creative auto";
                case 261: return "sunset";
            }
        }

        code = readInteger(CasioType2MakernoteDirectory.class, CasioType2MakernoteDirectory.TAG_RECORD_MODE);
        if (code.isPresent()) {
            switch (code.get()) {
                case  2: return "program";
                case  3: return "shutter priority";
                case  4: return "aperture priority";
                case  5: return "manual";
                case  6: return "best shot";
                case 17: // -v-
                case 19: return "movie";
            }
        }

        return null;
    }

    /**
     * Fetches a String from a directory.
     *
     * @param directory
     *            Directory to read from
     * @param tag
     *            Tag to be read
     * @return String that was read
     */
    protected <T extends Directory> Optional<String> readString(Class<T> directory, final int tag) {
        return metadata.getDirectoriesOfType(directory).stream()
                .filter(dir -> dir.containsTag(tag))
                .map(dir -> dir.getString(tag))
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Fetches a Rational from a directory.
     *
     * @param directory
     *            Directory to read from
     * @param tag
     *            Tag to be read
     * @return Rational that was read
     */
    protected <T extends Directory> Optional<Rational> readRational(Class<T> directory, final int tag) {
        return metadata.getDirectoriesOfType(directory).stream()
                .filter(dir -> dir.containsTag(tag))
                .map(dir -> dir.getRational(tag))
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Fetches an Integer from a directory.
     *
     * @param directory
     *            Directory to read from
     * @param tag
     *            Tag to be read
     * @return Integer that was read
     */
    protected <T extends Directory> Optional<Integer> readInteger(Class<T> directory, final int tag) {
        return metadata.getDirectoriesOfType(directory).stream()
                        .filter(dir -> dir.containsTag(tag))
                        .map(dir -> dir.getInteger(tag))
                        .filter(Objects::nonNull)
                        .findFirst();
    }

    /**
     * Fetches a {@link Date} from a directory.
     *
     * @param directory
     *            Directory to read from
     * @param tag
     *            Tag to be read
     * @param tz
     *            TimeZone the camera is configured to
     * @return Date that was read, or {@code null} if there was no such information
     */
    protected <T extends Directory> Optional<Date> readDate(Class<T> directory, final int tag, final TimeZone tz) {
        return metadata.getDirectoriesOfType(directory).stream()
                .filter(dir -> dir.containsTag(tag))
                .map(dir -> dir.getDate(tag, tz))
                .filter(Objects::nonNull)
                .findFirst();
    }

    /**
     * Converts an angle from a directory. The implementation handles one to three (and
     * even more) rational array entries.
     *
     * @param directory
     *            Directory to read from
     * @param tag
     *            Tag to be read
     * @return BigDecimal containing the angle, probably rounded
     */
    protected <T extends Directory> Optional<BigDecimal> readAngle(Class<T> directory, final int tag) {
        return metadata.getDirectoriesOfType(directory).stream()
                .filter(dir -> dir.containsTag(tag))
                .map(dir -> {
                    Rational[] data = dir.getRationalArray(tag);
                    if (data == null) {
                        return null;
                    }

                    double result = 0d;
                    for (int ix = data.length - 1; ix >= 0; ix--) {
                        result = (result / 60d) + data[ix].doubleValue();
                    }

                    return BigDecimal.valueOf(result).setScale(6, RoundingMode.HALF_DOWN);
                })
                .filter(Objects::nonNull)
                .findFirst();
    }

}
