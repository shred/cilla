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
 * A simple implementation of {@link SocialLink} that just stores values.
 *
 * @author Richard "Shred" Körber
 */
public class SimpleSocialLink implements SocialLink {

    private final String url;
    private final String iconUrl;
    private final String title;

    /**
     * Instantiates a new simple social link.
     *
     * @param url
     *            Target URL
     * @param iconUrl
     *            Icon URL
     * @param title
     *            Name of the social bookmark service
     */
    public SimpleSocialLink(String url, String iconUrl, String title) {
        this.url = url;
        this.iconUrl = iconUrl;
        this.title = title;
    }

    @Override
    public String getUrl()                      { return url; }

    @Override
    public String getIconUrl()                  { return iconUrl; }

    @Override
    public String getTitle()                    { return title; }

}
