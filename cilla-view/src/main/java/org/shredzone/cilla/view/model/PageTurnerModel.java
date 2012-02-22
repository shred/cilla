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
package org.shredzone.cilla.view.model;

import java.io.Serializable;

import org.shredzone.cilla.core.model.Page;

/**
 * Contains the neighbouring pages of a page.
 *
 * @author Richard "Shred" Körber
 */
public class PageTurnerModel implements Serializable {
    private static final long serialVersionUID = 8116373117940760584L;

    private final Page previous;
    private final Page next;

    /**
     * Creates a new {@link PageTurnerModel}.
     *
     * @param previous
     *            previous {@link Page}, or {@code null} if there is none
     * @param next
     *            next {@link Page}, or {@code null} if there is none
     */
    public PageTurnerModel(Page previous, Page next) {
        this.previous = previous;
        this.next = next;
    }

    /**
     * Previous {@link Page}, or {@code null} if there is none.
     */
    public Page getPrevious()                   { return previous; }

    /**
     * Next {@link Page}, or {@code null} if there is none.
     */
    public Page getNext()                       { return next; }

}
