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
package org.shredzone.cilla.ws.exception;

/**
 * This exception is thrown when a parameter given to a service call was bad.
 *
 * @author Richard "Shred" Körber
 */
public class CillaParameterException extends CillaServiceException {
    private static final long serialVersionUID = 315582015557037042L;

    /**
     * Creates a new {@link CillaParameterException}.
     *
     * @param msg
     *            hint about what parameter was bad, and why
     */
    public CillaParameterException(String msg) {
        super(msg);
    }

}
