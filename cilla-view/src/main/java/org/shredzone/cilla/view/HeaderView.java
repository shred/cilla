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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.service.HeaderService;
import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.cilla.web.plugin.manager.ImageProcessingManager;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.annotation.Optional;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewGroup;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;

/**
 * Views for generating all files about header images.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class HeaderView extends AbstractView {

    private @Resource HeaderService headerService;
    private @Resource ImageProcessingManager imageProcessingManager;

    /**
     * Shows details about a header (like a detailled caption and the location it was
     * taken).
     */
    @Framed
    @View(pattern = "/header/${header.id}/${#simplify(header.name)}.html", signature = {"header"})
    public String headerView(
            @PathPart("header.id") Header header,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        if (!headerService.isVisible(header)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        req.setAttribute("headerImage", header);

        return "view/headerDetails.jsp";
    }

    /**
     * Shows a map of the location the header picture was taken at.
     */
    @Framed
    @View(pattern = "/header/${header.id}/map/${#simplify(header.name)}.html", name="header.map")
    public String headerMapView(
            @PathPart("header.id") Header header,
            HttpServletRequest req)
    throws ViewException {
        if (!headerService.isVisible(header)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        req.setAttribute("headerImage", header);

        return "view/headerMap.jsp";
    }

    /**
     * Shows a list of all published headers.
     */
    @Framed
    @View(pattern = "/header/index.html")
    public String headerListView(HttpServletRequest req)
    throws ViewException {
        req.setAttribute("heads", headerService.getVisibleHeaders());
        return "view/headerList.jsp";
    }

    /**
     * Streams the header image.
     */
    @ViewGroup({
        @View(pattern = "/header/image/${header.id}-${#type}.${#suffix(header.headerImage.contentType)}"),
        @View(pattern = "/header/image/${header.id}.${#suffix(header.headerImage.contentType)}")
    })
    public void headerImageView(
            @PathPart("header.id") Header header,
            @Optional @PathPart("#type") String type,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        if (!headerService.isVisible(header)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        ImageProcessing ip = null;
        if (type != null) {
            ip = imageProcessingManager.createImageProcessing(type);
            if (ip == null) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_FOUND);
            }
        }

        ResourceDataSource ds = headerService.getHeaderImage(header, ip);
        streamDataSource(ds, req, resp);
    }

    /**
     * Streams the uncropped header image.
     */
    @ViewGroup({
        @View(pattern = "/header/full/${header.id}-${#type}.${#suffix(header.headerImage.contentType)}"),
        @View(pattern = "/header/full/${header.id}.${#suffix(header.headerImage.contentType)}")
    })
    public void headerUncroppedView(
            @PathPart("header.id") Header header,
            @Optional @PathPart("#type") String type,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        if (!headerService.isVisible(header)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        ImageProcessing ip = null;
        if (type != null) {
            ip = imageProcessingManager.createImageProcessing(type);
            if (ip == null) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_FOUND);
            }
        }

        ResourceDataSource ds = headerService.getFullImage(header, ip);
        streamDataSource(ds, req, resp);
    }

}
