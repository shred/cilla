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
package org.shredzone.cilla.web.header;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.shredzone.cilla.web.header.tag.HeadTag;
import org.shredzone.cilla.web.header.tag.MetaTag;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Collects tags for the HTML document header.
 * <p>
 * This spring bean is request scoped.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("request")
public class DocumentHeader {

    private SortedSet<MetaTag> metaTags = new TreeSet<MetaTag>();
    private List<HeadTag> headTags = new ArrayList<HeadTag>();
    private boolean setup = false;

    /**
     * Adds a new {@link HeadTag} to the HTML document.
     *
     * @param tag
     *            {@link HeadTag} to be added. If it is a {@link MetaTag}, it will replace
     *            a {@link MetaTag} with the same name.
     */
    public void add(HeadTag tag) {
        if (tag instanceof MetaTag) {
            MetaTag mt = (MetaTag) tag;
            metaTags.remove(mt); // remove if it's already present
            metaTags.add(mt);
        } else {
            headTags.add(tag);
        }
    }

    /**
     * Checks if the document header already contains a {@link MetaTag} with the same
     * name.
     *
     * @param tag
     *            {@link MetaTag} to check for
     * @return {@code true} if there is another {@link MetaTag} with the same name
     */
    public boolean contains(MetaTag tag) {
        return metaTags.contains(tag);
    }

    /**
     * Returns all {@link HeadTag} that have been added. The tags are returned in the
     * sequence they have been added. {@link MetaTag} are not included.
     */
    public Collection<HeadTag> getHeadTags() {
        return Collections.unmodifiableCollection(headTags);
    }

    /**
     * Returns all {@link MetaTag} that have been added. The tags are returned sorted by
     * their name. Other {@link HeadTag} are not included.
     */
    public Collection<MetaTag> getMetaTags() {
        return Collections.unmodifiableCollection(metaTags);
    }

    /**
     * Checks if the {@link DocumentHeader} is set up for this request. For internal use.
     */
    public boolean isSetup() {
        return setup;
    }

    /**
     * Notifies this {@link DocumentHeader} that it was set up. For internal use.
     */
    public void setup() {
        setup = true;
    }

}
