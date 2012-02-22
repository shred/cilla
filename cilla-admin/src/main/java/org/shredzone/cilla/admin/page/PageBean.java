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
package org.shredzone.cilla.admin.page;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.PageDto;
import org.shredzone.cilla.ws.page.PageInfoDto;
import org.shredzone.cilla.ws.page.PageWs;
import org.shredzone.cilla.ws.page.SectionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A bean for editing {@link PageDto}.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class PageBean {

    private @Value("${maxProposedSubjects}") int maxProposedSubjects;

    private @Resource PageWs pageWs;
    private @Resource Collection<PageSelectionObserver> pageSelectionObservers;

    private PageDto page;
    private UIInput sectionUi;

    /**
     * The {@link PageDto} being edited.
     */
    public PageDto getPage()                    { return page; }
    public void setPage(PageDto page)           {
        this.page = page;
        for (PageSelectionObserver observer : pageSelectionObservers) {
            observer.onPageSelected(page);
        }
    }

    /**
     * The {@link PageInfoDto} for the {@link PageDto} being edited.
     * <p>
     * Setting the {@link PageInfoDto} will also set the {@link PageDto}.
     */
    public PageInfoDto getPageInfo()            { return getPage(); }
    public void setPageInfo(PageInfoDto dto)    {
        if (dto != null) {
            try {
                setPage(pageWs.fetch(dto.getId()));
            } catch (CillaServiceException ex) {
                setPage(null);
            }
        } else {
            setPage(null);
        }
    }

    /**
     * UI Binding for the section type selector.
     */
    public UIInput getNewSectionBinding()       { return sectionUi; }
    public void setNewSectionBinding(UIInput selectBinding) { this.sectionUi = selectBinding; }

    /**
     * Adds a new section to the current page. The section type is selected by the UI
     * component set at {@link #setNewSectionBinding(UIInput)}.
     */
    public void addNewSection() throws CillaServiceException {
        String newType = sectionUi.getValue().toString();
        SectionDto section = pageWs.createNewSection(newType);
        page.getSections().add(section);
        FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * Adds a new {@link PageDto}.
     */
    public String add() throws CillaServiceException {
        setPage(pageWs.createNew());
        return "/admin/page/edit.xhtml";
    }

    /**
     * Edits the currently selected {@link PageDto}.
     */
    public String edit() throws CillaServiceException {
        if (page == null) {
            return null;
        }

        return "/admin/page/edit.xhtml";
    }

    /**
     * Deletes the currently selected {@link PageDto}.
     */
    public void delete() throws CillaServiceException {
        if (page == null) {
            return;
        }

        pageWs.delete(page.getId());
        setPage(null);
    }

    /**
     * Commits changes to the currently selected {@link PageDto} and returns to the page
     * overview.
     */
    public String commit() throws CillaServiceException {
        setPage(pageWs.commit(page));
        return "/admin/page/list.xhtml";
    }

    /**
     * Commits changes to the currently selected {@link PageDto}, but stays at the page
     * editor.
     */
    public String save() throws CillaServiceException {
        setPage(pageWs.commit(page));
        return "/admin/page/edit.xhtml";
    }

    /**
     * Cancels editing the currently selected {@link PageDto}, throwing away all changes.
     */
    public String cancelEdit() throws CillaServiceException {
        setPage(pageWs.fetch(page.getId()));
        return "/admin/page/list.xhtml";
    }

    /**
     * Gets the preview URL of the currently selected {@link PageDto}.
     */
    public String getPageUrl() throws CillaServiceException {
        return pageWs.previewUrl(page.getId());
    }

    /**
     * Sets the publication date of the currently selected {@link PageDto} to now.
     */
    public void publishNow() {
        page.setPublication(new Date());
        FacesContext.getCurrentInstance().renderResponse();
    }

    /**
     * Proposes a list of subjects to the given query string.
     *
     * @param query
     *            Query string
     * @return A (limited) list of proposals best matching the query string
     */
    public List<String> proposeSubjects(String query) {
        return pageWs.proposeSubjects(query, maxProposedSubjects);
    }

}
