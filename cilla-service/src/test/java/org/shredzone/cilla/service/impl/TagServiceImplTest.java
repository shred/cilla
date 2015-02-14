/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2014 Richard "Shred" Körber
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
package org.shredzone.cilla.service.impl;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Tag;

/**
 * Unit test for {@link TagServiceImpl}.
 *
 * @author Richard "Shred" Körber
 */
public class TagServiceImplTest {

    private static long tagSequence = 1;
    private static long pageSequence = 1;

    @Test
    public void tagCloudTest() {
        long limit = 1_000_000L;
        long beginning = 1408652722000L - limit;

        // Setup Tags
        Tag t1 = createTag("apple");
        Tag t2 = createTag("cherry");
        Tag t3 = createTag("grapefruit");
        Tag t4 = createTag("lemon");
        Tag t5 = createTag("cranberry");

        // Setup test Pages
        List<Page> pages = new ArrayList<>();
        pages.add(createPage(beginning +   1_000L, t1, t2));
        pages.add(createPage(beginning +  10_000L, t3, t1));
        pages.add(createPage(beginning + 100_000L, t2, t4));
        pages.add(createPage(beginning + 800_000L, t5));

        TagServiceImpl tsi = new TagServiceImpl();
        Map<Tag, Float> cloudMap = tsi.computeTagCloud(pages, beginning, limit);

        assertThat(cloudMap.size(), is(5));
        assertThat(cloudMap.get(t1), is(0.01375f));
        assertThat(cloudMap.get(t2), is(0.12625f));
        assertThat(cloudMap.get(t3), is(0.01375f));
        assertThat(cloudMap.get(t4), is(0.12625f));
        assertThat(cloudMap.get(t5), is(1.0f));
    }

    private static Page createPage(long publication, Tag... tags) {
        Page page = new Page();
        page.setId(pageSequence++);
        page.setPublication(new Date(publication));
        page.getTags().addAll(Arrays.asList(tags));
        return page;
    }

    private static Tag createTag(String name) {
        Tag tag = new Tag();
        tag.setId(tagSequence++);
        tag.setName(name);
        return tag;
    }

}
