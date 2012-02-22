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
package org.shredzone.cilla.admin.page;

import java.util.List;

import javax.annotation.Resource;

import org.shredzone.cilla.ws.page.PageDto;
import org.shredzone.cilla.ws.tag.TagWs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Bean helping to choose a tag for the current page.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("session")
public class PageTagBean implements PageSelectionObserver {

    private @Value("${maxProposedTags}") int maxProposedTags;

    private @Resource TagWs tagWs;

    private String tag = "";

    /**
     * Currently selected tag.
     */
    public String getTag()                      { return tag; }
    public void setTag(String tag)              { this.tag = tag; }

    /**
     * Clears the currently selected tag.
     */
    public void clear() {
        tag = "";
    }

    /**
     * Returns a proposal of existing tags matching the query.
     *
     * @param query
     *            Query string
     * @return Limited list of proposed tags
     */
    public List<String> complete(String query) {
        return tagWs.proposeTags(query, maxProposedTags);
    }

    @Override
    public void onPageSelected(PageDto selectedPage) {
        clear();
    }

}
