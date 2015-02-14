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

import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.core.model.Medium;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.MediumDao;
import org.shredzone.cilla.service.PageService;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.view.annotation.Framed;
import org.shredzone.cilla.web.plugin.LocalLinkResolver;
import org.shredzone.cilla.web.plugin.manager.ImageProcessingManager;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.annotation.Optional;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.PageNotFoundException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;

/**
 * Views for streaming media.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class MediaView extends AbstractView implements LocalLinkResolver {

    private @Resource PageService pageService;
    private @Resource MediumDao mediaDao;
    private @Resource ImageProcessingManager imageProcessingManager;

    /**
     * Streams a medium of the given page.
     */
    @View(pattern = "/page/${page.id}/${#type}/${#name}", signature = {"page", "#type", "#name"})
    @View(pattern = "/page/${page.id}/${#name}", signature = {"page", "#name"})
    public void mediumView(
            @PathPart("page.id") Page page,
            @Optional @PathPart("#type") String type,
            @PathPart("#name") String name,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException, CillaServiceException {
        if (!pageService.isVisible(page)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        Medium media = mediaDao.fetchByName(page, name);
        if (media == null) {
            throw new PageNotFoundException();
        }

        ImageProcessing ip = null;
        if (type != null) {
            ip = imageProcessingManager.createImageProcessing(type);
            if (ip == null) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_FOUND);
            }
        }

        ResourceDataSource ds = pageService.getMediumImage(media, ip);
        streamDataSource(ds, req, resp);
    }

    /**
     * Shows the full view of a medium for the given page.
     */
    @Framed
    @View(pattern = "/page/${page.id}/show/${#name}", name = "showPreview")
    public String mediumFullView(
            @PathPart("page.id") Page page,
            @PathPart("#name") String name,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        if (!pageService.isVisible(page)) {
            throw new ErrorResponseException(HttpServletResponse.SC_FORBIDDEN);
        }

        Medium media = mediaDao.fetchByName(page, name);
        if (media == null) {
            throw new PageNotFoundException();
        }

        req.setAttribute("page", page);
        req.setAttribute("media", media);

        return "view/media.jsp";
    }

    @Override
    public String resolveLocalLink(String url, boolean image, Supplier<LinkBuilder> linkBuilderSupplier) {
        String[] parts = url.split("/", 2);

        if (image && parts.length == 1) {
            return linkBuilderSupplier.get()
                            .param("name", parts[0])
                            .toString();

        } else if (image && parts.length == 2) {
            return linkBuilderSupplier.get()
                            .param("type", parts[0])
                            .param("name", parts[1])
                            .toString();

        } else if (!image && parts.length == 2) {
            if ("show".equals(parts[0])) {
                return linkBuilderSupplier.get()
                            .view("showPreview")
                            .param("name", parts[1])
                            .toString();
            }
        }

        return null;
    }

}
