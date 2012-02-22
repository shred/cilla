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
import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * A service for complex {@link Page} operations.
 *
 * @author Richard "Shred" Körber
 */
public interface PageService {

    /**
     * Creates a new, empty page.
     *
     * @return created {@link Page}
     */
    Page createNew();

    /**
     * Adds a new page.
     *
     * @param page
     *            {@link Page} to be added.
     */
    void create(Page page);

    /**
     * Must be invoked after page content has been updated externally.
     *
     * @param page
     *            {@link Page} that was updated.
     */
    void update(Page page);

    /**
     * Removes a page with all its dependencies.
     *
     * @param page
     *            {@link Page} to be removed.
     */
    void remove(Page page) throws CillaServiceException;

    /**
     * Creates a new, empty {@link Medium} object.
     */
    Medium createNewMedium();

    /**
     * Checks if this page is visible to the currently logged in user.
     * <p>
     * Does not check challenge/response restrictions.
     *
     * @param page
     *            {@link Page} to check
     * @return {@code true} if the user is allowed to see this page
     */
    boolean isVisible(Page page);

    /**
     * Checks if the response is acceptable for the page's challenge.
     *
     * @param page
     *            {@link Page} to check
     * @param response
     *            Response given for the page's challenge
     * @return {@code true} if the response is good, {@code false} otherwise
     */
    boolean isAcceptedResponse(Page page, String response);

    /**
     * Adds a medium to the page.
     *
     * @param page
     *            {@link Page} to add a medium to
     * @param medium
     *            {@link Medium} object to be added
     * @param source
     *            {@link DataSource} providing the medium
     */
    void addMedium(Page page, Medium medium, DataSource source) throws CillaServiceException;

    /**
     * Updates a medium of a page.
     *
     * @param page
     *            {@link Page} the medium belongs to
     * @param medium
     *            {@link Medium} object to be updated
     * @param source
     *            {@link DataSource} providing the medium, or {@code null} to keep the
     *            medium
     */
    void updateMedium(Page page, Medium medium, DataSource source) throws CillaServiceException;

    /**
     * Removes a medium from a page.
     *
     * @param page
     *            {@link Page} the medium belongs to
     * @param medium
     *            {@link Medium} object to be removed
     */
    void removeMedium(Page page, Medium medium) throws CillaServiceException;

    /**
     * Gets a medium of a page.
     *
     * @param medium
     *            {@link Medium} to stream
     * @param process
     *            {@link ImageProcessing} for processing images, {@code null} for original
     * @return {@link ResourceDataSource} of that picture
     */
    ResourceDataSource getMediumImage(Medium medium, ImageProcessing process) throws CillaServiceException;

    /**
     * Updates the published state of all pages, triggering the appropriate events where
     * necessary.
     */
    void updatePublishedState();

}
