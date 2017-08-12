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
package org.shredzone.cilla.admin.page.gallery;

import javax.activation.DataHandler;
import javax.annotation.Resource;

import org.primefaces.model.StreamedContent;
import org.shredzone.cilla.admin.AbstractImageBean;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.PageWs;
import org.shredzone.cilla.ws.page.PictureDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Provides gallery images.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class GalleryImageBean extends AbstractImageBean {
    private static final long serialVersionUID = -620675494847533591L;

    @Resource
    private transient GalleryBean galleryBean;

    @Resource
    private transient PageWs pageWs;

    @Resource @Qualifier("thumb")
    private transient ImageProcessing thumbIp;

    /**
     * Gets the thumbnail of a gallery image. It is selected by its index in the current
     * section's picture list.
     */
    public StreamedContent getThumbnail() throws CillaServiceException {
        Integer index = getFacesIndex();
        if (index == null) {
            return createEmptyStreamedContent();
        }

        PictureDto pic = galleryBean.getPictures().get(index);
        DataHandler dh = pic.getUploadFile();
        if (dh != null) {
            return createStreamedContent(dh, thumbIp);
        } else if (pic.isPersisted()) {
            return createStreamedContent(pageWs.getGalleryImage(pic.getId(), thumbIp));
        } else {
            return createEmptyStreamedContent();
        }
    }

}
