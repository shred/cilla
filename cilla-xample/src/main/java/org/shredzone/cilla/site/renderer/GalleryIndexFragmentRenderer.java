/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2013 Richard "Shred" Körber
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
package org.shredzone.cilla.site.renderer;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.web.comment.CommentThreadModel;
import org.shredzone.cilla.web.comment.CommentThreadService;
import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentItem;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.web.fragment.manager.FragmentContext;
import org.springframework.stereotype.Component;

/**
 * Renders the picture index of a gallery.
 *
 * @author Richard "Shred" Körber
 */
@FragmentRenderer
@Component
public class GalleryIndexFragmentRenderer {

    // Maximum number of preview images (unless tags are activated)
    private static final int maxPreviewPictures = 5;

    private @Resource CommentThreadService commentThreadService;

    @Fragment
    public void galleryShortFragment(
        @FragmentItem GallerySection gallery,
        FragmentContext context
    ) throws IOException {
        Tag tag = getTagFilter(gallery, context);
        context.setAttribute("pictureTag", tag);

        List<Picture> pictures = gallery.getPictures();
        context.setAttribute("pictureCount", pictures.size());

        int picIndex = 0;
        int picCount = 0;
        boolean skipped = false;
        for (Picture pic : pictures) {
            picIndex++;

            if (tag != null && !(pic.getTags().contains(tag))) {
                // When filtered by Tag, skip this Picture if it does not contain it.
                skipped = true;
                continue;
            }

            picCount++;
            if (tag == null && picCount > maxPreviewPictures) {
                skipped = true;
                break;
            }

            if (skipped) {
                context.include("fragment/picture/short-bullets.jspf");
                skipped = false;
            }

            context.setAttribute("picture", pic);
            context.setAttribute("pictureIndex", picIndex);
            context.include("fragment/picture/short-thumb.jspf");
        }

        if (skipped) {
            context.include("fragment/picture/short-bullets.jspf");
        }
    }

    @Fragment
    public void galleryIndexFragment(
        @FragmentItem GallerySection gallery,
        FragmentContext context
    ) throws IOException {
        List<Picture> pictures = gallery.getPictures();
        context.setAttribute("pictureCount", pictures.size());

        int picIndex = 0;
        for (Picture pic : pictures) {
            picIndex++;

            CommentThreadModel thread = commentThreadService.getCommentThread(pic);

            context.setAttribute("picture", pic);
            context.setAttribute("pictureIndex", picIndex);
            context.setAttribute("thread", thread);
            context.include("fragment/picture/index-thumb.jspf");
        }
    }

    /**
     * Gets the {@link Tag} to filter the pictures with. Only pictures being tagged with
     * the current search result tag will be returned.
     *
     * @param gallery
     *            {@link GallerySection} to be rendered
     * @param context
     *            {@link FragmentContext}
     * @return {@link Tag} to filter the pictures with, or {@code null} to show all
     *         pictures
     */
    private Tag getTagFilter(GallerySection gallery, FragmentContext context) {
        Object result = context.getAttribute("result");
        if (result != null && result instanceof SearchResult) {
            FilterModel filter = ((SearchResult) result).getFilter();
            if (filter != null) {
                Tag tag = filter.getTag();

                if (tag != null && gallery.getPage().getTags().contains(tag)) {
                    // When the page contains the tag, show all pictures!
                    return null;
                }

                return tag;
            }
        }

        return null;
    }

}
