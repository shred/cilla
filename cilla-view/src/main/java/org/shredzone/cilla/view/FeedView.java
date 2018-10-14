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

import static java.util.stream.Collectors.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
import org.shredzone.cilla.view.interceptor.FeedViewInterceptor;
import org.shredzone.cilla.web.FeedType;
import org.shredzone.cilla.web.format.TextFormatter;
import org.shredzone.cilla.web.plugin.manager.PriorityComparator;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.commons.view.ViewContext;
import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.PageNotFoundException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndCategoryImpl;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.feed.synd.SyndLinkImpl;
import com.rometools.rome.feed.synd.SyndPerson;
import com.rometools.rome.feed.synd.SyndPersonImpl;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedOutput;

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
    private @Resource ApplicationContext applicationContext;

    private List<FeedViewInterceptor> interceptors;

    /**
     * Initializes the list of feed view interceptors.
     */
    @PostConstruct
    protected void setup() {
        interceptors = applicationContext.getBeansOfType(FeedViewInterceptor.class).values().stream()
                .sorted(new PriorityComparator<>(FeedViewInterceptor.class))
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * A feed for all pages.
     */
    @View(pattern = "/feed/list.${#feed}", signature = {"date", "#feed"})
    @View(pattern = "/feed/list.${#feed}", signature = {"#feed"})
    public void feedListView(
            @PathPart("#feed") String suffix,
            HttpServletRequest req, HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        fetchPages(filter, context, req, resp, selfUrl, "list", convertFeedType(suffix));
    }

    /**
     * A feed for a single category.
     */
    @View(pattern = "/feed/category/${category.id}/${#simplify(category.name)}.${#feed}", signature = {"category", "date", "#feed"})
    @View(pattern = "/feed/category/${category.id}/${#simplify(category.name)}.${#feed}", signature = {"category", "#feed"})
    public void feedCategoryView(
            @PathPart("category.id") Category category,
            @PathPart("#feed") String suffix,
            HttpServletRequest req, HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().category(category).param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        filter.setCategory(category);
        fetchPages(filter, context, req, resp, selfUrl, "category-" + category.getId(), convertFeedType(suffix));
    }

    /**
     * A feed for a single tag.
     */
    @View(pattern = "/feed/tag/${#encode(tag.name)}.${#feed}", signature = {"tag", "date", "#feed"})
    @View(pattern = "/feed/tag/${#encode(tag.name)}.${#feed}", signature = {"tag", "#feed"})
    public void feedTagView(
            @PathPart("#encode(tag.name)") Tag tag,
            @PathPart("#feed") String suffix,
            HttpServletRequest req, HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().tag(tag).param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        filter.setTag(tag);
        fetchPages(filter, context, req, resp, selfUrl, "tag-" + tag.getName(), convertFeedType(suffix));
    }

    /**
     * A feed for a single author.
     */
    @View(pattern = "/feed/author/${author.id}/${#simplify(author.name)}.${#feed}", signature = {"author", "date", "#feed"})
    @View(pattern = "/feed/author/${author.id}/${#simplify(author.name)}.${#feed}", signature = {"author", "#feed"})
    public void feedAuthorView(
            @PathPart("author.id") User user,
            @PathPart("#feed") String suffix,
            HttpServletRequest req, HttpServletResponse resp, ViewContext context)
    throws ViewException, CillaServiceException {
        String selfUrl = linkService.linkTo().author(user).param("feed", suffix).absolute().toString();
        FilterModel filter = new FilterModel();
        filter.setCreator(user);
        fetchPages(filter, context, req, resp, selfUrl, "author-" + user.getId(), convertFeedType(suffix));
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
        return Arrays.stream(FeedType.values())
                .filter(type -> type.getSuffix().equals(suffix))
                .map(FeedType::getType)
                .findFirst()
                .orElseThrow(() -> new PageNotFoundException("Unknown feed type " + suffix));
    }

    /**
     * Fetches the pages by query and returns a feed.
     *
     * @param filter
     *            {@link FilterModel} with the query parameters
     * @param context
     *            {@link ViewContext} with the view context
     * @param req
     *            the {@link HttpServletRequest}
     * @param resp
     *            the {@link HttpServletResponse}
     * @param selfUrl
     *            freshly generated link to this feed
     * @param feedId
     *            a unique id for this feed
     * @param type
     *            feed type, any type supported by ROME.
     */
    private void fetchPages(FilterModel filter, ViewContext context,
        HttpServletRequest req, HttpServletResponse resp,
        String selfUrl, String feedId, String type)
    throws ViewException, CillaServiceException {
        Date lastModified = pageDao.fetchMinMaxModification()[1];

        if (isNotModifiedSince(req, lastModified)) {
            throw new ErrorResponseException(HttpServletResponse.SC_NOT_MODIFIED);
        }

        SearchResult result = searchService.search(filter);
        result.setPaginator(new PaginatorModel(maxEntries));

        String contentType = "application/xml";
        if (type.startsWith("atom_")) {
            contentType = "application/atom+xml";
        } else if (type.startsWith("rss_")) {
            contentType = "application/rss+xml";
        }

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
        feed.getEntries().addAll(result.getPages().stream()
                .filter(page -> !page.isRestricted()) // do not show restricted pages
                .filter(page -> interceptors.stream().noneMatch(it -> it.isIgnored(page)))
                .map(page -> createEntry(page, uriPrefix))
                .collect(Collectors.toList()));

        resp.setContentType(contentType);
        resp.setDateHeader("Last-Modified", lastModified.getTime());
        resp.setCharacterEncoding("utf-8");

        try {
            SyndFeedOutput output = new SyndFeedOutput();
            output.output(feed, resp.getWriter());
        } catch (FeedException | IOException ex) {
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
    private SyndEntry createEntry(Page page, String uriPrefix) {
        String pageUrl = linkService.linkTo().page(page).absolute().toString();

        String body = textFormatter.format(
                page.getTeaser(),
                () -> linkService.linkTo().page(page)
        ).toString();

        for (FeedViewInterceptor it : interceptors) {
            body = it.filterDescription(page, body);
        }

        SyndEntry entry = new SyndEntryImpl();
        entry.setTitle(page.getTitle());
        entry.setLink(pageUrl);
        entry.setUri(buildTaxonomyUri(uriPrefix, "page", page.getId()));
        entry.setPublishedDate(page.getPublication());
        entry.setUpdatedDate(page.getModification());

        // Add a donation link if set
        if (page.isDonatable() && page.getDonateUrl() != null) {
            SyndLink link = new SyndLinkImpl();
            link.setRel("payment");
            link.setHref(page.getDonateUrl());
            entry.getLinks().add(link);
        }

        SyndContent description = new SyndContentImpl();
        description.setType("text/html");
        description.setValue(body);
        entry.setDescription(description);

        entry.getCategories().addAll(page.getCategories().stream()
                .map(cat -> {
                    SyndCategory category = new SyndCategoryImpl();
                    category.setName(cat.getName());
                    category.setTaxonomyUri(buildTaxonomyUri(uriPrefix, "category", cat.getId()));
                    return category;
                })
                .collect(toList()));

        entry.getCategories().addAll(page.getTags().stream()
                .map(tag -> {
                    SyndCategory category = new SyndCategoryImpl();
                    category.setName(tag.getName());
                    category.setTaxonomyUri(buildTaxonomyUri(uriPrefix, "tag", tag.getName()));
                    return category;
                })
                .collect(toList()));

        List<SyndPerson> authorList = new ArrayList<>();
        User creator = page.getCreator();
        SyndPerson author = new SyndPersonImpl();
        author.setName(creator.getName());
        author.setUri(buildTaxonomyUri(uriPrefix, "author", creator.getId()));
        authorList.add(author);
        entry.setAuthors(authorList);

        interceptors.forEach(it -> it.postProcessEntry(page, entry));

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
            throw new InternalError(ex);
        }
    }

}
