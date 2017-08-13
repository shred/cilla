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
package org.shredzone.cilla.core.repository.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.CategoryDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link CategoryDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("categoryDao")
@Transactional
public class CategoryDaoHibImpl extends BaseDaoHibImpl<Category> implements CategoryDao {

    @Override
    public Category fetch(long id) {
        return getCurrentSession().get(Category.class, id);
    }

    @Override
    public List<Category> fetchAll() {
        return getCurrentSession().createQuery("FROM Category ORDER BY name", Category.class).list();
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession().createQuery("SELECT COUNT(*) FROM Category",
                        Number.class);
        return q.uniqueResult().longValue();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Category.class);
    }

    @Override
    public Category fetchRootCategory(Category category) {
        Category result = category;
        while (result.getParent() != null) {
            result = result.getParent();
        }
        return result;
    }

    @Override
    public List<Category> fetchRootCategories() {
        return getCurrentSession().createQuery("FROM Category WHERE parent_id IS NULL ORDER BY sequence", Category.class).list();
    }

    @Override
    public Collection<Category> fetchRootCategoriesOfPage(Page page) {
        return page.getCategories().stream()
                .map(this::fetchRootCategory)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<Category> fetchCategoryTree(Category root) {
        Set<Category> result = new HashSet<>();
        fetchCategoryTreeRecursive(root, result);
        return result;
    }

    /**
     * Recursively fetch a category tree.
     *
     * @param parent
     *            Parent {@link Category}
     * @param result
     *            Set of {@link Category} to be filled up
     */
    private void fetchCategoryTreeRecursive(Category parent, Set<Category> result) {
        result.add(parent);
        for (Category child : parent.getChildren()) {
            fetchCategoryTreeRecursive(child, result);
        }
    }

}
