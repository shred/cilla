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

import java.util.List;

import javax.activation.DataSource;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * A service that takes care for header images.
 *
 * @author Richard "Shred" Körber
 */
public interface HeaderService {

    /**
     * Creates a new, empty header.
     *
     * @return {@link Header} that was created
     */
    Header createNew();

    /**
     * Adds a new {@link Header} object to the database.
     *
     * @param header
     *            {@link Header} to be added
     * @param headerImg
     *            The uploaded file to be used as header image
     * @param fullImg
     *            The uploaded file to be used as a full view of the image in the header's
     *            description page
     */
    void create(Header header, DataSource headerImg, DataSource fullImg) throws CillaServiceException;

    /**
     * Updates a {@link Header}. Must be invoked after a header was changed.
     *
     * @param header
     *            {@link Header} that was changed
     */
    void update(Header header) throws CillaServiceException;

    /**
     * Updates the images of an existing {@link Header}.
     *
     * @param header
     *            {@link Header} to change the images of
     * @param headerImg
     *            The uploaded file to be used as header image, or {@code null} to keep
     *            the existing image
     * @param fullImg
     *            The uploaded file to be used as a full view of the image in the header's
     *            description page, or {@code null} to keep the existing image
     */
    void updateImage(Header header, DataSource headerImg, DataSource fullImg)
        throws CillaServiceException;

    /**
     * Gets the header image.
     *
     * @param header
     *            {@link Header} to get the image of
     * @param process
     *            {@link ImageProcessing} for processing images, {@code null} for original
     * @return {@link ResourceDataSource} of the image
     */
    ResourceDataSource getHeaderImage(Header header, ImageProcessing process) throws CillaServiceException;

    /**
     * Gets a scaled instance of the full sized header image.
     *
     * @param header
     *            {@link Header} to get the image of
     * @param process
     *            {@link ImageProcessing} for processing images, {@code null} for original
     * @return {@link ResourceDataSource} of the image
     */
    ResourceDataSource getFullImage(Header header, ImageProcessing process) throws CillaServiceException;

    /**
     * Removes a header, deleting its resources.
     *
     * @param header
     *            {@link Header} to be removed
     */
    void remove(Header header) throws CillaServiceException;

    /**
     * Checks if the header is visible to the currently logged in user.
     *
     * @param header
     *            {@link Header} to check
     * @return {@code true} if the header is visible
     */
    boolean isVisible(Header header);

    /**
     * Returns a collection of all {@link Header} that are visible to the currently logged
     * in user.
     *
     * @return List of {@link Header}
     */
    List<Header> getVisibleHeaders();

}
