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
package org.shredzone.cilla.web.renderer;

import java.io.IOException;
import java.util.Locale;

import org.shredzone.cilla.service.search.PaginatorModel;

/**
 * For the paginator to work, a single Spring bean must implement
 * {@link PaginatorRendererStrategy}. Its task is to create the proper HTML output for the
 * paginator.
 * <p>
 * <em>NOTE:</em> {@link PaginatorRendererStrategy} implementations must be prototype scoped!
 *
 * @author Richard "Shred" Körber
 */
public interface PaginatorRendererStrategy {

    /**
     * Sets the {@link Locale} to be used in this paginator.
     */
    void setLocale(Locale locale);

    /**
     * Computes the next page number to be shown.
     *
     * @param model
     *            {@link PaginatorModel} with information about the paginator
     * @param current
     *            current page number to calculate the next number for (starting with 0)
     * @return next page number. If {@code current} or -1 is returned, the paginator
     *         sequence is stopped
     */
    int computeNextPage(PaginatorModel model, int current);

    /**
     * Opens the HTML container of the paginator.
     *
     * @param out
     *            Target to write to
     */
    void openContainer(Appendable out) throws IOException;

    /**
     * Closes the HTML container of the paginator.
     *
     * @param out
     *            Target to write to
     */
    void closeContainer(Appendable out) throws IOException;

    /**
     * Writes the "previous" link that jumps to the previous page.
     *
     * @param out
     *            Target to write to
     * @param url
     *            Link to the previous page, or {@code null} if there is no previous page
     */
    void previousLink(Appendable out, String url) throws IOException;

    /**
     * Writes the "next" link that jumps to the next page.
     *
     * @param out
     *            Target to write to
     * @param url
     *            Link to the next page, or {@code null} if there is no next page
     */
    void nextLink(Appendable out, String url) throws IOException;

    /**
     * Writes a link to a page.
     *
     * @param out
     *            Target to write to
     * @param url
     *            Link to the current page
     * @param current
     *            Current page number (starting from 0)
     * @param selected
     *            Selected page number (starting from 0)
     * @param last
     *            Last page number (starting from 0)
     */
    void pageLink(Appendable out, String url, int current, int selected, int last) throws IOException;

}
