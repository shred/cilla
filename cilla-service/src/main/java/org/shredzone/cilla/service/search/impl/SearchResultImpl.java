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
package org.shredzone.cilla.service.search.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.CategoryDao;
import org.shredzone.cilla.core.util.DateUtils;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.service.search.strategy.SearchStrategy;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implements {@link SearchResult}. This bean is prototype scoped.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Transactional(readOnly = true)
public class SearchResultImpl implements SearchResult {

    private @Resource SearchStrategy simpleSearchStrategy;
    private @Resource SearchStrategy luceneSearchStrategy;
    private @Resource CategoryDao categoryDao;

    private Date now = new Date();

    private FilterModel filter;
    private PaginatorModel paginator;

    private Integer count;
    private List<Page> pages;
    private List<String> highlighted;
    private Collection<Category> effectiveCategories;

    @Override
    public FilterModel getFilter() {
        return filter;
    }

    @Override
    public void setFilter(FilterModel filter) {
        this.filter = filter;
        this.now = new Date();
        this.count = null;
        this.pages = null;
        this.highlighted = null;
        this.effectiveCategories = null;
    }

    @Override
    public PaginatorModel getPaginator() {
        return paginator;
    }

    @Override
    public void setPaginator(PaginatorModel paginator) {
        this.paginator = paginator;
        this.pages = null;
        this.highlighted = null;
    }

    private SearchStrategy getSearchStrategy() {
        if (filter != null && filter.getQuery() != null) {
            return luceneSearchStrategy;
        } else {
            return simpleSearchStrategy;
        }
    }

    @Override
    public int getCount() throws CillaServiceException {
        if (count == null) {
            getSearchStrategy().count(this);
        }
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public List<Page> getPages() throws CillaServiceException {
        if (pages == null) {
            getSearchStrategy().search(this);
        }
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    @Override
    public List<String> getHighlighted() throws CillaServiceException {
        if (highlighted == null) {
            getSearchStrategy().search(this);
        }
        return highlighted;
    }

    public void setHighlighted(List<String> highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public Collection<Category> getEffectiveCategories() throws CillaServiceException {
        if (filter.getCategory() == null) {
            return null;
        }

        if (effectiveCategories == null) {
            effectiveCategories = categoryDao.fetchCategoryTree(filter.getCategory());
        }

        return effectiveCategories;
    }

    @Override
    public Set<Integer> fetchPageDays(Calendar calendar) throws CillaServiceException {
        if (calendar == null) {
            return Collections.emptySet();
        }

        return getSearchStrategy().fetchPageDays(this, calendar);
    }

    public Date getNow() {
        return DateUtils.cloneDate(now);
    }

}
