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
package org.shredzone.cilla.admin.page.media;

import java.util.List;

import javax.annotation.Resource;
import javax.faces.component.UIComponent;

import org.primefaces.event.FileUploadEvent;
import org.shredzone.cilla.admin.UploadedFileDataSource;
import org.shredzone.cilla.admin.page.PageSelectionObserver;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.MediumDto;
import org.shredzone.cilla.ws.page.PageDto;
import org.shredzone.cilla.ws.page.PageWs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean for handling {@link MediumDto} of the currently selected page.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class PageMediaBean implements PageSelectionObserver {

    private @Resource PageWs pageWs;

    private PageDto page;
    private MediumDto[] selection;
    private UIComponent uiTable;

    @Override
    public void onPageSelected(PageDto selectedPage) {
        this.page = selectedPage;
        this.selection = null;
    }

    /**
     * Array of the currently selected {@link MediumDto}.
     */
    public void setSelectedMedia(MediumDto[] selection) { this.selection = selection; }
    public MediumDto[] getSelectedMedia()        { return selection; }

    /**
     * {@link UIComponent} of the table showing the media images.
     */
    public UIComponent getUiTable()             { return uiTable; }
    public void setUiTable(UIComponent uiTable) { this.uiTable = uiTable; }

    /**
     * Returns all {@link MediumDto} of the current page.
     */
    public List<MediumDto> getMedia()            { return page.getMedia(); }

    /**
     * Deletes all currently selected {@link MediumDto}.
     */
    public void delete() {
        if (selection != null) {
            for (MediumDto selected : selection) {
                page.getMedia().remove(selected);
            }
        }
        selection = null;
    }

    /**
     * Handles upload of a new media.
     */
    public void handleFileUpload(FileUploadEvent event) throws CillaServiceException {
        MediumDto dto = pageWs.createNewMedium();
        dto.setName(event.getFile().getFileName());
        dto.setUploadMediumFile(new UploadedFileDataSource(event.getFile()).toDataHandler());
        page.getMedia().add(dto);
    }

}
