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
package org.shredzone.cilla.service.link;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.commons.view.PathType;
import org.shredzone.commons.view.ViewService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Builds a link to the given target, by trying to find a view with the matching name or
 * signature.
 * <p>
 * Many methods return a reference to the builder class, so the calls can be
 * daisy-chained.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LinkBuilder {

    private @Resource ViewService viewService;

    private String view;
    private StringBuilder searchpart;
    private String anchor;
    private String baseUrl;
    private CillaPathContext data = new CillaPathContext();
    private PathType type = PathType.RELATIVE;

    /**
     * Target view name.
     */
    public LinkBuilder view(String view) {
        this.view = view;
        return this;
    }

    /**
     * Target {@link Page}.
     */
    public LinkBuilder page(Page page) {
        data.setPage(page);
        return this;
    }

    /**
     * Target {@link Category}.
     */
    public LinkBuilder category(Category category) {
        data.setCategory(category);
        return this;
    }

    /**
     * Target {@link Tag}.
     */
    public LinkBuilder tag(Tag tag) {
        data.setTag(tag);
        return this;
    }

    /**
     * Target {@link User}.
     */
    public LinkBuilder author(User author) {
        data.setAuthor(author);
        return this;
    }

    /**
     * Target {@link Section}.
     */
    public LinkBuilder section(Section section) {
        data.setSection(section);
        return this;
    }

    /**
     * Target {@link Picture}.
     */
    public LinkBuilder picture(Picture picture) {
        data.setPicture(picture);
        return this;
    }

    /**
     * Target {@link Header}.
     */
    public LinkBuilder header(Header header) {
        data.setHeader(header);
        return this;
    }

    /**
     * Sets a date range.
     */
    public LinkBuilder date(DateRange date) {
        data.setDate(date);
        return this;
    }

    /**
     * Adds a generic parameter.
     *
     * @param name  Parameter name
     * @param value  Parameter value
     */
    public LinkBuilder param(String name, Object value) {
        data.setParam(name, value);
        return this;
    }

    /**
     * Adds a search part parameter.
     *
     * @param name
     *            Parameter name, will be properly encoded
     * @param value
     *            Parameter value, will be properly encoded
     */
    public LinkBuilder query(String name, String value) {
        if (searchpart == null) {
            searchpart = new StringBuilder();
        } else {
            searchpart.append('&');
        }
        searchpart.append(urlencode(name));
        searchpart.append('=');
        searchpart.append(urlencode(value));
        return this;
    }

    /**
     * Sets an anchor at the target URL.
     */
    public LinkBuilder anchor(String anchor) {
        this.anchor = anchor;
        return this;
    }

    /**
     * Sets a {@link Linkable} as reference.
     */
    public LinkBuilder ref(Linkable link) {
        if (link != null) {
            link.transfer(this);
        }
        return this;
    }

    /**
     * Sets a base URL for absolute links.
     */
    public LinkBuilder base(String url) {
        if (url.endsWith("/")) {
            this.baseUrl = url.substring(0, url.length() - 1);
        } else {
            this.baseUrl = url;
        }
        this.type = PathType.VIEW;
        return this;
    }

    /**
     * Sets the link qualifier.
     */
    public LinkBuilder qualifier(String qualifier) {
        data.setQualifier(qualifier);
        return this;
    }

    /**
     * Generates an absolute (instead of a relative) target URL.
     */
    public LinkBuilder absolute() {
        this.type = PathType.ABSOLUTE;
        return this;
    }

    /**
     * Builds the URL.
     */
    @Override
    public String toString() {
        String path = viewService.buildPath(data, view, type);

        if (baseUrl != null) {
            path = baseUrl + path;
        }

        if (searchpart != null) {
            path = path + '?' + searchpart.toString();
        }

        if (anchor != null) {
            path = path + '#' + anchor;
        }

        return path;
    }

    /**
     * URL encodes the given string, using utf-8 encoding.
     * <p>
     * This is just a wrapper of {@link URLEncoder#encode(String, String)} with the
     * exception being handled internally.
     *
     * @param url
     *            URL to encode
     * @return encoded URL
     */
    private static String urlencode(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError("no utf-8");
        }
    }

}
