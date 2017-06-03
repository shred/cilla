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
import java.util.Locale;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.service.link.Linkable;
import org.shredzone.cilla.ws.PageOrder;

/**
 * Contains filter parameters.
 * <p>
 * It is {@link Linkable} and can be passed to the {@link LinkBuilder} in order to
 * propagate the filter settings to the next request.
 *
 * @author Richard "Shred" Körber
 */
public class FilterModel implements Linkable, Serializable {
    private static final long serialVersionUID = 2119596896526404505L;

    private Category category;
    private User creator;
    private Tag tag;
    private Page page;
    private DateRange date;
    private String query;
    private Locale locale;
    private PageOrder order;
    private boolean ascending;

    public FilterModel() {
        // Default constructor
    }

    /**
     * Clone constructor.
     *
     * @param model
     *            {@link FilterModel} to be cloned
     */
    public FilterModel(FilterModel model) {
        this.category = model.category;
        this.creator = model.creator;
        this.tag = model.tag;
        this.page = model.page;
        this.date = (model.date != null ? new DateRange(model.date) : null);
        this.query = model.query;
        this.locale = model.locale;
        this.order = model.order;
        this.ascending = model.ascending;
    }

    /**
     * Only find pages with this effective {@link Category}. It will also match pages
     * that are categorized with at least one subcategory of this {@link Category}.
     */
    public Category getCategory()               { return category; }
    public void setCategory(Category category)  { this.category = category; }

    /**
     * Only find pages created by this {@link User}.
     */
    public User getCreator()                    { return creator; }
    public void setCreator(User creator)        { this.creator = creator; }

    /**
     * Only find pages tagged with this {@link Tag}.
     */
    public Tag getTag()                         { return tag; }
    public void setTag(Tag tag)                 { this.tag = tag; }

    /**
     * Only find this {@link Page}.
     */
    public Page getPage()                       { return page; }
    public void setPage(Page page)              { this.page = page; }

    /**
     * Only find pages within the given {@link DateRange}.
     */
    public DateRange getDate()                  { return date; }
    public void setDate(DateRange date)         { this.date = date; }

    /**
     * Only find pages with the content matching the given query string.
     */
    public String getQuery()                    { return query; }
    public void setQuery(String query)          { this.query = query; }

    /**
     * If a query string is set, use this {@link Locale} to optimize the search result.
     */
    public Locale getLocale()                   { return locale; }
    public void setLocale(Locale locale)        { this.locale = locale; }

    /**
     * The desired result order. If not set, the standard order is used.
     */
    public PageOrder getOrder()                 { return order; }
    public void setOrder(PageOrder order)       { this.order = order; }

    /**
     * Ascending result order. If not set, the default descending order is used.
     */
    public boolean isAscending()                { return ascending; }
    public void setAscending(boolean ascending) { this.ascending = ascending; }

    @Override
    public void transfer(LinkBuilder builder) {
        if (page != null) {
            // Single pages are rendered in a separate view, so do not add further
            // attributes to the link
            builder.page(page);
            return;
        }

        if (category != null) builder.category(category);
        if (creator != null) builder.author(creator);
        if (tag != null) builder.tag(tag);
        if (date != null) builder.date(date);
        if (query != null) builder.view("search").query("q", query);
    }

}
