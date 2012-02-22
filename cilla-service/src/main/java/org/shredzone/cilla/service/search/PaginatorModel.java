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
package org.shredzone.cilla.service.search;

import java.io.Serializable;

/**
 * Model for showing a Paginator.
 *
 * @author Richard "Shred" Körber
 */
public class PaginatorModel implements Serializable {
    private static final long serialVersionUID = -8587632813299434164L;

    private int selectedPage;
    private int count;
    private int perPage;

    public PaginatorModel() {
        // Default constructor
    }

    /**
     * Creates a new {@link PaginatorModel} with the given number of entries per page.
     *
     * @param perPage
     *            number of entries per page
     */
    public PaginatorModel(int perPage) {
        this.perPage = perPage;
    }

    /**
     * Creates a new {@link PaginatorModel} with the given number of entries per page, and
     * the selected page.
     *
     * @param perPage
     *            number of entries per page
     * @param selectedPage
     *            selected page number
     */
    public PaginatorModel(int perPage, int selectedPage) {
        this.perPage = perPage;
        this.selectedPage = selectedPage;
    }

    /**
     * Copy constructor.
     *
     * @param src
     *            {@link PaginatorModel} to make a copy of
     */
    public PaginatorModel(PaginatorModel src) {
        this.selectedPage = src.selectedPage;
        this.count = src.count;
        this.perPage = src.perPage;
    }

    /**
     * Selected page. Counting from 0.
     */
    public int getSelectedPage()                { return selectedPage; }
    public void setSelectedPage(int selectedPage) { this.selectedPage = selectedPage; }

    /**
     * Number of entries available.
     */
    public int getCount()                       { return count; }
    public void setCount(int count)             { this.count = count; }

    /**
     * Number of entries per page.
     */
    public int getPerPage()                     { return perPage; }
    public void setPerPage(int perPage)         { this.perPage = perPage; }

    /**
     * Returns the first entry to be displayed.
     */
    public int getFirst() {
        return selectedPage * perPage;
    }

    /**
     * Returns the number of pages available.
     */
    public int getPageCount() {
        if (perPage > 0) {
            return (count + perPage - 1) / perPage;
        } else {
            return 0;
        }
    }

    /**
     * Returns {@code true} if the selected page is the first page.
     */
    public boolean isFirstPage() {
        return selectedPage == 0;
    }

    /**
     * Returns {@code true} if the selected page is the last page;
     */
    public boolean isLastPage() {
        return selectedPage >= (getPageCount() - 1);
    }

    /**
     * Returns {@code true} if there are no entries to be rendered.
     */
    public boolean isEmpty() {
        return count == 0;
    }

}
