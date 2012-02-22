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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.primefaces.model.DualListModel;
import org.shredzone.cilla.admin.WrappedCillaException;
import org.shredzone.cilla.ws.category.CategoryDto;
import org.shredzone.cilla.ws.category.CategoryWs;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.page.PageDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Handles a pick list of categories for the current page.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class CategoryPicklistBean implements PageSelectionObserver {

    private @Resource CategoryWs categoryWs;

    private PageDto page;
    private List<CategoryDto> allCategories;
    private List<CategoryDto> selectedCategories;

    @Override
    public void onPageSelected(PageDto selectedPage) {
        this.page = selectedPage;

        if (page != null) {
            try {
                allCategories = categoryWs.list();
                selectedCategories = new ArrayList<CategoryDto>(page.getCategories());
                allCategories.removeAll(selectedCategories);
            } catch (CillaServiceException ex) {
                throw new WrappedCillaException(ex);
            }
        } else {
            allCategories = null;
            selectedCategories = null;
        }
    }

    /**
     * Returns a new {@link DualListModel} to be used for category selection.
     */
    public DualListModel<CategoryDto> getModel() {
        return new DualListModel<CategoryDto>(allCategories, selectedCategories);
    }

    /**
     * Sets the selected categories from the given {@link DualListModel}.
     *
     * @param model
     *            the selected categories of this {@link DualListModel} are set at the
     *            currently selected page
     */
    public void setModel(DualListModel<CategoryDto> model) {
        if (page != null) {
            page.setCategories(model.getTarget());
        }
    }

}
