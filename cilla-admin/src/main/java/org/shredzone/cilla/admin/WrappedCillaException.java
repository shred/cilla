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

import org.shredzone.cilla.ws.exception.CillaServiceException;

/**
 * Wraps a {@link CillaServiceException} into a {@link RuntimeException}.
 *
 * @author Richard "Shred" Körber
 */
public class WrappedCillaException extends RuntimeException {
    private static final long serialVersionUID = 1075325327453730574L;

    public WrappedCillaException(CillaServiceException ex) {
        super(ex);
    }

}
