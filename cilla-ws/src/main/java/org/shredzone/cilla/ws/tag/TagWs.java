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
package org.shredzone.cilla.ws.tag;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Service for managing Tags.
 *
 * @author Richard "Shred" Körber
 */
@WebService
public interface TagWs {

    /**
     * Counts all tags.
     */
    long count();

    /**
     * Lists all tags.
     *
     * @return List of all tags, in alphabetical order
     */
    List<String> list();

    /**
     * Proposes a list of tags for the given text. No more than limit tags will be
     * returned.
     *
     * @param query
     *            Keyword to propose tags for
     * @param limit
     *            maximum number of proposals
     * @return List of Tags for the keyword. May be empty, but never {@code null}. Never
     *         contains more than limit entries.
     */
    List<String> proposeTags(@WebParam(name = "query") String query,
            @WebParam(name = "limit") int limit);

}
