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
package org.shredzone.cilla.view;

import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;

/**
 * Views for security related pages.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class SecurityView extends AbstractView {

    /**
     * Shows the login page.
     */
    @Framed
    @View(pattern = "/login", name="login")
    public String pageView()
    throws ViewException {
        return "view/login.jsp";
    }

}
