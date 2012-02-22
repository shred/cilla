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
package org.shredzone.cilla.web.social;

/**
 * A social link describes a link to a social bookmark service.
 *
 * @author Richard "Shred" Körber
 */
public interface SocialLink {

    /**
     * Returns the target URL to send a bookmark to the social service.
     *
     * @return Target URL
     */
    String getUrl();

    /**
     * Returns the URL of an icon that represents the social bookmark service.
     *
     * @return Icon URL
     */
    String getIconUrl();

    /**
     * Returns the name of the social bookmark service.
     *
     * @return Service name
     */
    String getTitle();

}
