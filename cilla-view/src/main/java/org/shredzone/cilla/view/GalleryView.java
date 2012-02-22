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

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.service.PageService;
import org.shredzone.cilla.service.PictureService;
import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.cilla.view.model.PictureInfoModel;
import org.shredzone.cilla.web.page.ResourceLockManager;
import org.shredzone.cilla.web.plugin.manager.ImageProcessingManager;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.annotation.Optional;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.Qualifier;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewGroup;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.PageNotFoundException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;

/**
 * Views for showing the standalone parts of the gallery section.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class GalleryView extends AbstractView {

    private @Resource PageService pageService;
    private @Resource PictureService pictureService;
    private @Resource ResourceLockManager unlockService;
    private @Resource ImageProcessingManager imageProcessingManager;

    /**
     * Shows a single picture of a gallery.
     */
    @Framed
    @ViewGroup({
        @View(pattern = "/show/gallery/${section.id}/picture/${picture.id}.html", signature = {"section", "picture"}),
        @View(pattern = "/ajax/gallery/${section.id}/picture/${picture.id}.html", signature = {"section", "picture"}, qualifier = "ajax")
    })
    public String galleryPictureView(
            @PathPart("section.id") GallerySection section,
            @PathPart("picture.id") Picture picture,
            @Qualifier String qualifier,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        if (!pageService.isVisible(section.getPage())) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        List<Picture> pictureList = section.getPictures();
        int size = pictureList.size();
        int current = pictureList.indexOf(picture);

        if (current < 0 || size == 0) {
            // There is such a picture, but not in this gallery!
            throw new PageNotFoundException("No such picture in this gallery.");
        }

        if (redirectRestricted(section.getPage(), req, resp)) {
            return null;
        }

        req.setAttribute("section", section);
        req.setAttribute("picture", picture);
        req.setAttribute("info", new PictureInfoModel(pictureList, current));

        if ("ajax".equals(qualifier)) {
            return "section/gallery/picture-ajax.jsp";
        } else {
            return "section/gallery/picture.jsp";
        }
    }

    /**
     * Shows a map of the location the picture was taken.
     */
    @Framed
    @View(pattern = "/show/gallery/${section.id}/map/${picture.id}.html", name="gallery.map")
    public String galleryMapView(
            @PathPart("section.id") GallerySection section,
            @PathPart("picture.id") Picture picture,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        if (!pageService.isVisible(section.getPage())) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        if (!section.getPictures().contains(picture)) {
            // There is such a picture, but not in this gallery!
            throw new PageNotFoundException("No such picture in this gallery.");
        }

        if (redirectRestricted(section.getPage(), req, resp)) {
            return null;
        }

        req.setAttribute("section", section);
        req.setAttribute("picture", picture);

        return "section/gallery/map.jsp";
    }

    /**
     * Streams the picture of a gallery.
     */
    @ViewGroup({
        @View(pattern = "/picture/${picture.id}-${#type}.${#suffix(picture.image.contentType)}", signature = {"picture", "#type"}),
        @View(pattern = "/picture/${picture.id}.${#suffix(picture.image.contentType)}", signature = {"picture"})
    })
    public void pictureView(
            @PathPart("picture.id") Picture picture,
            @Optional @PathPart("#type") String type,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        // Take measures against deep linking of the pictures. No need to check page
        // permissions, as the picture is not unlocked if the visitor had no access
        // to the page.
        if (!unlockService.isUnlocked(req.getSession(), picture)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        ImageProcessing ip = null;
        if (type != null) {
            ip = imageProcessingManager.createImageProcessing(type);
            if (ip == null) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_FOUND);
            }
        }

        ResourceDataSource ds = pictureService.getImage(picture, ip);
        streamDataSource(ds, req, resp);
    }

}
