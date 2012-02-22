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
package org.shredzone.cilla.core.search;

/**
 * This service takes care for maintaining the search index.
 *
 * @author Richard "Shred" Körber
 */
public interface IndexerService {

    /**
     * Invalidates the entire search index, and rebuilds it from scratch.
     * <p>
     * This call is asynchronous. It is safe to invoke this method even when the index is
     * currently being built in the background.
     * <p>
     * On large sites, reindexing may take a considerable amount of time and cause high
     * database load. During reindexing, the search index is locked.
     */
    void reindex();

}
