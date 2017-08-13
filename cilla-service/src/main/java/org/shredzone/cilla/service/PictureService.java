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
package org.shredzone.cilla.service;

import javax.activation.DataSource;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Service for handling {@link Picture}.
 *
 * @author Richard "Shred" Körber
 */
public interface PictureService {

    /**
     * Creates a new, empty {@link Picture} object to be used in a gallery.
     */
    Picture createNew();

    /**
     * Adds a {@link Picture} to the gallery.
     * <p>
     * The image passed in is analyzed and it's EXIF data, geolocation data, creation
     * date, dimensions and type are written into the Picture entity.
     * <p>
     * Picture's sequence numbers needs to be set separately.
     *
     * @param section
     *            {@link GallerySection} to add the picture to. The picture will be the
     *            last on the list.
     * @param picture
     *            {@link Picture} to add.
     * @param source
     *            {@link DataSource} containing a stream to the picture.
     */
    void addPicture(GallerySection section, Picture picture, DataSource source)
    throws CillaServiceException;

    /**
     * Updates a picture.
     * <p>
     * The image passed in is analyzed and it's EXIF data, geolocation data, creation
     * date, dimensions and type are written into the Picture entity.
     * <p>
     * Picture's sequence numbers needs to be set separately.
     *
     * @param picture
     *            {@link Picture} to be updated
     * @param source
     *            {@link DataSource} containing a stream to the picture, or {@code null}
     *            to keep the current picture
     */
    void updatePicture(Picture picture, DataSource source)
    throws CillaServiceException;

    /**
     * Removes a picture. It is removed from the {@link GallerySection}.
     * <p>
     * Picture's sequence numbers needs to be set separately.
     *
     * @param picture
     *            {@link Picture} to remove
     */
    void removePicture(Picture picture) throws CillaServiceException;

    /**
     * Renumbers all pictures of a gallery.
     *
     * @param gallery
     *            {@link GallerySection} to renumber pictures of
     */
    void renumberPictures(GallerySection gallery) throws CillaServiceException;

    /**
     * Gets a picture of a gallery.
     *
     * @param picture
     *            {@link Picture} to stream
     * @return {@link ResourceDataSource} of that picture
     */
    ResourceDataSource getImage(Picture picture) throws CillaServiceException;

}
