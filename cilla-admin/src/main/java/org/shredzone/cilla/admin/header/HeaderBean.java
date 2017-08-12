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

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.shredzone.cilla.admin.EditableMapModel;
import org.shredzone.cilla.admin.MapModelFactory;
import org.shredzone.cilla.admin.UploadedFileDataSource;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.header.HeaderDto;
import org.shredzone.cilla.ws.header.HeaderWs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for handling {@link HeaderDto}.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class HeaderBean implements Serializable {
    private static final long serialVersionUID = 6288999431849895025L;

    @Resource
    private transient HeaderWs headerWs;

    @Resource
    private transient MapModelFactory mapModelFactory;

    private HeaderDto header;
    private EditableMapModel headerMapModel;

    /**
     * Currently selected {@link HeaderDto}.
     */
    public HeaderDto getHeader()                { return header;  }
    public void setHeader(HeaderDto header)     {
        this.header = header;
        this.headerMapModel = mapModelFactory.createMapModel(header);
    }

    /**
     * {@link EditableMapModel} of the current header.
     */
    public EditableMapModel getHeaderMapModel() { return headerMapModel; }

    /**
     * Creates a new {@link HeaderDto}.
     */
    public String add() throws CillaServiceException {
        setHeader(headerWs.createNew());
        return "/admin/header/edit.xhtml";
    }

    /**
     * Edits the selected {@link HeaderDto}.
     */
    public String edit() throws CillaServiceException {
        if (header == null) {
            return null;
        }

        return "/admin/header/edit.xhtml";
    }

    /**
     * Deletes the selected {@link HeaderDto}.
     */
    public void delete() throws CillaServiceException {
        if (header == null) {
            return;
        }

        headerWs.delete(header.getId());
        header = null;
        headerMapModel = null;
    }

    /**
     * Commits the changes to the selected or newly created {@link HeaderDto}.
     */
    public String commit() throws CillaServiceException {
        if (header.getId() == 0) {
            FacesContext ctx = FacesContext.getCurrentInstance();
            Locale loc = ctx.getViewRoot().getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(ctx.getApplication().getMessageBundle(), loc);

            boolean missing = false;

            if (header.getUploadHeaderFile() == null) {
                FacesMessage message = new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                bundle.getString("header.edit.error.image"),
                                null
                );
                ctx.addMessage("editForm:headerPicture", message);
                missing = true;
            }

            if (header.getUploadFullFile() == null) {
                FacesMessage message = new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                bundle.getString("header.edit.error.full"),
                                null
                );
                ctx.addMessage("editForm:headerFullPicture", message);
                missing = true;
            }

            if (missing) {
                // at least one file was missing, abort...
                return null;
            }
        }

        setHeader(headerWs.commit(header));

        return "/admin/header/list.xhtml";
    }

    /**
     * Handles file uploads to the header image.
     *
     * @param event
     *            {@link FileUploadEvent} containing the uploaded image
     */
    public void handleHeaderFileUpload(FileUploadEvent event) {
        header.setUploadHeaderFile(new UploadedFileDataSource(event.getFile()).toDataHandler());
    }

    /**
     * Handles file uploads to the full-scale image.
     *
     * @param event
     *            {@link FileUploadEvent} containing the uploaded image
     */
    public void handleFullFileUpload(FileUploadEvent event) {
        header.setUploadFullFile(new UploadedFileDataSource(event.getFile()).toDataHandler());
    }

}
