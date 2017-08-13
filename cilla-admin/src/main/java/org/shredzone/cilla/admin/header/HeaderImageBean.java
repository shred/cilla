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
package org.shredzone.cilla.admin.header;

import javax.annotation.Resource;

import org.primefaces.model.StreamedContent;
import org.shredzone.cilla.admin.AbstractImageBean;
import org.shredzone.cilla.admin.processing.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.header.HeaderWs;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A bean that provides header images.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class HeaderImageBean extends AbstractImageBean {
    private static final long serialVersionUID = 2122584473681755274L;

    @Resource
    private transient HeaderBean headerBean;

    @Resource
    private transient HeaderWs headerWs;

    @Resource @Qualifier("preview")
    private transient ImageProcessing previewIp;

    @Resource @Qualifier("thumb")
    private transient ImageProcessing thumbIp;

    /**
     * Returns a persisted header as a thumbnail image. The header is selected by a
     * "renderId" request parameter.
     */
    public StreamedContent getThumbnail() throws CillaServiceException {
        Long renderId = getFacesRenderId();
        if (renderId != null) {
            return createStreamedContent(headerWs.getHeaderImage(renderId), previewIp);
        } else {
            return createEmptyStreamedContent();
        }
    }

    /**
     * Returns the currently selected header as a thumbnail image.
     */
    public StreamedContent getHeaderView() throws CillaServiceException {
        if (headerBean.getHeader().getUploadHeaderFile() != null) {
            return createStreamedContent(headerBean.getHeader().getUploadHeaderFile(), previewIp);
        } else if (headerBean.getHeader().isPersisted()) {
            return createStreamedContent(headerWs.getHeaderImage(headerBean.getHeader().getId()), previewIp);
        } else {
            return createEmptyStreamedContent();
        }
    }

    /**
     * Returns the full-size view of the currently selected header as a thumbnail image.
     */
    public StreamedContent getFullView() throws CillaServiceException {
        if (headerBean.getHeader().getUploadFullFile() != null) {
            return createStreamedContent(headerBean.getHeader().getUploadFullFile(), thumbIp);
        } else if (headerBean.getHeader().isPersisted()) {
            return createStreamedContent(headerWs.getFullImage(headerBean.getHeader().getId()), thumbIp);
        } else {
            return createEmptyStreamedContent();
        }
    }

}
