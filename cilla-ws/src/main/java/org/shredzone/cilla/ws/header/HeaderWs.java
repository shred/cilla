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
package org.shredzone.cilla.ws.header;

import java.util.List;

import javax.activation.DataHandler;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Service for managing Headers.
 *
 * @author Richard "Shred" Körber
 */
@WebService
public interface HeaderWs {

    /**
     * Fetches a {@link HeaderDto} by its ID.
     *
     * @param headerId
     *            Header ID
     * @return {@link HeaderDto}, or {@code null} if it does not exist
     */
    HeaderDto fetch(@WebParam(name = "headerId") long headerId) throws CillaServiceException;

    /**
     * Counts the number of all headers.
     */
    long count();

    /**
     * Lists all {@link HeaderDto} matching the criteria.
     *
     * @param criteria
     *            {@link ListRange}, or {@code null} for all
     * @return List of matching {@link HeaderDto}
     */
    List<HeaderDto> list(@WebParam(name = "criteria") ListRange criteria);

    /**
     * Creates a new {@link HeaderDto}.
     *
     * @return {@link HeaderDto} that was created
     */
    HeaderDto createNew() throws CillaServiceException;

    /**
     * Commits a {@link HeaderDto}.
     *
     * @param header
     *            {@link HeaderDto} to be committed
     * @return Committed {@link HeaderDto}, must be used from now on
     */
    HeaderDto commit(@WebParam(name = "header") HeaderDto header) throws CillaServiceException;

    /**
     * Deletes a Header by its ID.
     *
     * @param headerId
     *            Header ID to be deleted
     */
    void delete(@WebParam(name = "headerId") long headerId) throws CillaServiceException;

    /**
     * Returns the header image.
     *
     * @param headerId
     *            Header ID to read the image for
     * @return {@link DataHandler} containing the header image
     */
    DataHandler getHeaderImage(@WebParam(name = "headerId") long headerId) throws CillaServiceException;

    /**
     * Returns the full-scaled image shown at the header's detail page.
     *
     * @param headerId
     *            Header ID to read the full-scale image for
     * @return {@link DataHandler} containing the header image
     */
    DataHandler getFullImage(@WebParam(name = "headerId") long headerId) throws CillaServiceException;

}
