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
 * An abstract implementation of {@link PaginatorRendererStrategy} that does almost
 * nothing.
 *
 * @author Richard "Shred" Körber
 */
public abstract class AbstractPaginatorRendererStrategy implements PaginatorRendererStrategy {

    protected Locale locale;

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation just returns the next page.
     */
    @Override
    public int computeNextPage(PaginatorModel model, int current) {
        int lastPage = model.getPageCount() - 1;
        int nextPage = current + 1;
        return (nextPage <= lastPage ? nextPage : -1);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void openContainer(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void closeContainer(Appendable out) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void previousLink(Appendable out, String url) throws IOException {
        // Do nothing by default...
    }

    /**
     * {@inheritDoc}
     * <p>
     * The default implementation does nothing.
     */
    @Override
    public void nextLink(Appendable out, String url) throws IOException {
        // Do nothing by default...
    }

}
