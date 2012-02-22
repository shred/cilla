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
package org.shredzone.cilla.core.repository;

import java.util.Collection;
import java.util.List;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Page;

/**
 * DAO for {@link Category} entities.
 *
 * @author Richard "Shred" Körber
 */
public interface CategoryDao extends BaseDao<Category> {

    /**
     * Fetches the root {@link Category} of the given {@link Category}.
     *
     * @param category
     *            {@link Category} to fetch root category of
     * @return root {@link Category}
     */
    Category fetchRootCategory(Category category);

    /**
     * Fetches only the root {@link Category} elements, sorted by their sequence. Root
     * elements do not have a parent category.
     *
     * @return List of all root {@link Category} elements.
     */
    List<Category> fetchRootCategories();

    /**
     * Fetches the root {@link Category} elements of all categories the page belongs to.
     * <p>
     * This call is slow, results should be cached.
     *
     * @param page
     *            {@link Page} to find the root categories for
     * @return Collection of root {@link Category} of the {@link Page}.
     */
    Collection<Category> fetchRootCategoriesOfPage(Page page);

    /**
     * Fetches the root {@link Category} and all its children {@link Category}.
     * <p>
     * This call is slow, results should be cached.
     *
     * @param root
     *            Root {@link Category}
     * @return Collection of all {@link Category} within that subtree
     */
    Collection<Category> fetchCategoryTree(Category root);

}
