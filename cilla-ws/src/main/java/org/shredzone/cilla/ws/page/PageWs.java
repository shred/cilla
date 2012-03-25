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
package org.shredzone.cilla.ws.page;

import java.util.Collection;
import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * A service for managing pages.
 *
 * @author Richard "Shred" Körber
 */
@WebService
public interface PageWs {

    /**
     * Fetches a {@link PageDto} by its ID.
     *
     * @param pageId
     *            Page id
     * @return {@link PageDto}, or {@code null} if it does not exist
     */
    PageDto fetch(@WebParam(name = "pageId") long pageId) throws CillaServiceException;

    /**
     * Returns the number of all pages (including unpublished pages).
     */
    long count();

    /**
     * Lists all {@link PageInfoDto} matching the criteria. Unpublished pages are
     * included.
     *
     * @param criteria
     *            {@link ListRange}, or {@code null} for all
     * @return List of matching {@link PageInfoDto}
     */
    List<PageInfoDto> list(@WebParam(name = "criteria") ListRange criteria);

    /**
     * Creates a new, empty {@link PageDto}.
     *
     * @return {@link PageDto} that was created
     */
    PageDto createNew() throws CillaServiceException;

    /**
     * Commits a {@link PageDto}.
     *
     * @param page
     *            {@link PageDto} to be committed
     * @return Committed {@link PageDto}, must be used from now on
     */
    PageDto commit(@WebParam(name = "page") PageDto page) throws CillaServiceException;

    /**
     * Deletes a Page by its ID. All dependencies are also deleted: comments, sections,
     * media, gallery pictures etc.
     *
     * @param pageId
     *            Page ID to be deleted
     */
    void delete(@WebParam(name = "pageId") long pageId) throws CillaServiceException;

    /**
     * Returns the preview URL of a page.
     *
     * @param pageId
     *            Page ID to get the preview URL for
     * @return Preview URL, or {@code null} if there is no preview available for some
     *         reasons
     */
    String previewUrl(@WebParam(name = "pageId") long pageId) throws CillaServiceException;

    /**
     * Gets all available section types.
     */
    Collection<String> getSectionTypes();

    /**
     * Creates a new section of the given type.
     *
     * @param type
     *            Section type
     * @return {@link SectionDto} that was created
     */
    SectionDto createNewSection(@WebParam(name = "type") String type) throws CillaServiceException;

    /**
     * Creates a new {@link MediumDto}.
     *
     * @return {@link MediumDto} that was created
     */
    MediumDto createNewMedium() throws CillaServiceException;

    /**
     * Creates a new {@link PictureDto}.
     *
     * @return {@link PictureDto} that was created
     */
    PictureDto createNewPicture() throws CillaServiceException;

    /**
     * Returns the medium image.
     *
     * @param mediumId
     *            ID of the Medium
     * @param process
     *            {@link ImageProcessing} for post processing, {@code null} for original
     * @return {@link DataHandler} returning the medium
     */
    DataHandler getMediumImage(@WebParam(name = "mediumId") long mediumId,
            @WebParam(name = "process") ImageProcessing process) throws CillaServiceException;

    /**
     * Returns the gallery picture.
     *
     * @param pictureId
     *            ID of the Picture
     * @param process
     *            {@link ImageProcessing} for post processing, {@code null} for original
     * @return {@link DataHandler} returning the picture
     */
    DataHandler getGalleryImage(@WebParam(name = "pictureId") long pictureId,
            @WebParam(name = "process") ImageProcessing process) throws CillaServiceException;

    /**
     * Proposes a list of subjects for the given text. No more than limit subjects will be
     * returned.
     *
     * @param query
     *            Keyword to propose subjects for
     * @param limit
     *            maximum number of proposals
     * @return List of Subjects for the keyword. May be empty, but never {@code null}.
     *         Never contains more than limit entries.
     */
    List<String> proposeSubjects(@WebParam(name = "query") String query,
            @WebParam(name = "limit") int limit);

}
