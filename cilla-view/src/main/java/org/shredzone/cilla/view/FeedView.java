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
package org.shredzone.cilla.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.PaginatorModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.service.search.SearchService;
import org.shredzone.cilla.web.FeedType;
import org.shredzone.cilla.web.format.TextFormatter;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.ViewContext;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewGroup;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.PageNotFoundException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.syndication.feed.synd.SyndCategory;
import com.sun.syndication.feed.synd.SyndCategoryImpl;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.feed.synd.SyndLink;
import com.sun.syndication.feed.synd.SyndLinkImpl;
import com.sun.syndication.feed.synd.SyndPerson;
import com.sun.syndication.feed.synd.SyndPersonImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * Views for generating atom and rss feeds for categories, tags and users.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class FeedView extends AbstractView {

    private @Value("${feed.maxEntries}") int maxEntries;
    private @Value("${site.name}") String title;
    private @Value("${site.description}")String description;

    private @Resource SearchService searchService;
    private @Resource PageDao pageDao;
    private @Resource TextFormatter textFormatter;
    private @Resource LinkService linkService;

    /**
     * A feed for all pages.
     */
    @ViewGroup({
        @View(pattern = "/feed/list.${#feed}", signature = {"date", "#feed"}),
        @View(pattern = "/feed/list.${#feed}", signature = {"#feed"})
    })
    public void feedListView(
            @PathPart("#feed") String suffix,
            HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        fetchPages(filter, context, resp, selfUrl, "list", convertFeedType(suffix));
    }

    /**
     * A feed for a single category.
     */
    @ViewGroup({
        @View(pattern = "/feed/category/${category.id}/${#simplify(category.name)}.${#feed}", signature = {"category", "date", "#feed"}),
        @View(pattern = "/feed/category/${category.id}/${#simplify(category.name)}.${#feed}", signature = {"category", "#feed"})
    })
    public void feedCategoryView(
            @PathPart("category.id") Category category,
            @PathPart("#feed") String suffix,
            HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().category(category).param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        filter.setCategory(category);
        fetchPages(filter, context, resp, selfUrl, "category-" + category.getId(), convertFeedType(suffix));
    }

    /**
     * A feed for a single tag.
     */
    @ViewGroup({
        @View(pattern = "/feed/tag/${#encode(tag.name)}.${#feed}", signature = {"tag", "date", "#feed"}),
        @View(pattern = "/feed/tag/${#encode(tag.name)}.${#feed}", signature = {"tag", "#feed"})
    })
    public void feedTagView(
            @PathPart("#encode(tag.name)") Tag tag,
            @PathPart("#feed") String suffix,
            HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().tag(tag).param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        filter.setTag(tag);
        fetchPages(filter, context, resp, selfUrl, "tag-" + tag.getName(), convertFeedType(suffix));
    }

    /**
     * A feed for a single author.
     */
    @ViewGroup({
        @View(pattern = "/feed/author/${author.id}/${#simplify(author.name)}.${#feed}", signature = {"author", "date", "#feed"}),
        @View(pattern = "/feed/author/${author.id}/${#simplify(author.name)}.${#feed}", signature = {"author", "#feed"})
    })
    public void feedAuthorView(
            @PathPart("author.id") User user,
            @PathPart("#feed") String suffix,
            HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().author(user).param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        filter.setCreator(user);
        fetchPages(filter, context, resp, selfUrl, "author-" + user.getId(), convertFeedType(suffix));
    }

    /**
     * Converts the feed's suffix to a ROME type.
     *
     * @param suffix
     *            the feed's suffix
     * @return ROME type
     * @throws PageNotFoundException
     *             if the feed suffix is unknown
     */
    private String convertFeedType(String suffix) throws PageNotFoundException {
        for (FeedType type : FeedType.values()) {
            if (type.getSuffix().equals(suffix)) {
                return type.getType();
            }
        }
        throw new PageNotFoundException("Unknown feed type " + suffix);
    }

    /**
     * Fetches the pages by query and returns a feed.
     *
     * @param filter
     *            {@link FilterModel} with the query parameters
     * @param context
     *            {@link ViewContext} with the view context
     * @param resp
     *            the {@link HttpServletResponse}
     * @param selfUrl
     *            freshly generated link to this feed
     * @param feedId
     *            a unique id for this feed
     * @param type
     *            feed type, any type supported by ROME.
     */
    @SuppressWarnings("unchecked")
    private void fetchPages(FilterModel filter, ViewContext context, HttpServletResponse resp, String selfUrl, String feedId, String type)
    throws ViewException, CillaServiceException {
        SearchResult result = searchService.search(filter);
        result.setPaginator(new PaginatorModel(maxEntries));

        String contentType = "application/xml";
        if (type.startsWith("atom_")) {
            contentType = "application/atom+xml";
        } else if (type.startsWith("rss_")) {
            contentType = "application/rss+xml";
        }

        Date lastModified = pageDao.fetchMinMaxModification()[1];

        String uriPrefix = context.getRequestServerUrl() + '/';

        SyndFeed feed = new SyndFeedImpl();
        feed.setFeedType(type);
        feed.setTitle(title);
        feed.setDescription(description);
        feed.setLink(linkService.linkTo().absolute().toString());
        feed.setUri(uriPrefix + feedId);
        feed.setPublishedDate(lastModified);

        SyndLink selfLink = new SyndLinkImpl();
        selfLink.setRel("self");
        selfLink.setType(contentType);
        selfLink.setHref(selfUrl);
        feed.getLinks().add(selfLink);

        for (Page page : result.getPages()) {
            if (!page.isRestricted()) {
                // Do not show restricted pages
                feed.getEntries().add(createEntry(page, uriPrefix));
            }
        }

        resp.setContentType(contentType);
        resp.setDateHeader("Last-Modified", lastModified.getTime());
        resp.setCharacterEncoding("utf-8");

        try {
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, resp.getWriter());
        } catch (IOException ex) {
            throw new ViewException("Could not create feed", ex);
        } catch (FeedException ex) {
            throw new ViewException("Could not create feed", ex);
        }
    }

    /**
     * Creates a {@link SyndEntry} from the given {@link Page}.
     *
     * @param page
     *            {@link Page} to create a {@link SyndEntry} for
     * @param uriPrefix
     *            URI prefix for this website
     * @return {@link SyndEntry} that was created
     */
    @SuppressWarnings("unchecked")
    private SyndEntry createEntry(Page page, String uriPrefix) {
        String pageUrl = linkService.linkTo().page(page).absolute().toString();

        String body = textFormatter.format(
                page.getTeaser(),
                page
        ).toString();

        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(page.getTitle());
        entry.setLink(pageUrl);
        entry.setUri(buildTaxonomyUri(uriPrefix, "page", page.getId()));
        entry.setPublishedDate(page.getPublication());
        entry.setUpdatedDate(page.getModification());

        // Add a payment link if article is registered with Flattr
        if (page.getFlattr() != null) {
            SyndLink link = new SyndLinkImpl();
            link.setRel("payment");
            link.setHref(page.getFlattr().getFlattrUrl());
            entry.getLinks().add(link);
        }

        SyndContent description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue(body);
        entry.setDescription(description);

        for (Category cat : page.getCategories()) {
            SyndCategory category = new SyndCategoryImpl();
            category.setName(cat.getName());
            category.setTaxonomyUri(buildTaxonomyUri(uriPrefix, "category", cat.getId()));
            entry.getCategories().add(category);
        }

        for (Tag tag : page.getTags()) {
            SyndCategory category = new SyndCategoryImpl();
            category.setName(tag.getName());
            category.setTaxonomyUri(buildTaxonomyUri(uriPrefix, "tag", tag.getName()));
            entry.getCategories().add(category);
        }

        List<SyndPerson> authorList = new ArrayList<SyndPerson>();
        User creator = page.getCreator();
        SyndPerson author = new SyndPersonImpl();
        author.setName(creator.getName());
        author.setUri(buildTaxonomyUri(uriPrefix, "author", creator.getId()));
        authorList.add(author);
        entry.setAuthors(authorList);

        return entry;
    }

    /**
     * Builds a taxonomy URL.
     *
     * @param prefix
     *            URI prefix for this web site
     * @param type
     *            feed type (e.g. "author")
     * @param id
     *            unique id of the feed's entity
     * @return generated URI
     */
    private String buildTaxonomyUri(String prefix, String type, long id) {
        return buildTaxonomyUri(prefix, type, String.valueOf(id));
    }

    /**
     * Builds a taxonomy URL.
     *
     * @param prefix
     *            URI prefix for this web site
     * @param type
     *            feed type (e.g. "author")
     * @param id
     *            unique id String of the feed's entity
     * @return generated URI
     */
    private String buildTaxonomyUri(String prefix, String type, String id) {
        try {
            return prefix + type + '-' + URLEncoder.encode(id, "utf-8");
        } catch (UnsupportedEncodingException ex) {
            throw new InternalError("utf-8 missing");
        }
    }

}
