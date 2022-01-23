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
package org.shredzone.cilla.service.link;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.service.search.DateRange;
import org.shredzone.commons.view.PathContext;
import org.shredzone.commons.view.Signature;

/**
 * Collects all data for the link builder.
 *
 * @author Richard "Shred" Körber
 */
public class CillaPathContext implements PathContext {

    private String qualifier;
    private Page page;
    private Category category;
    private Tag tag;
    private User author;
    private Section section;
    private DateRange date;
    private Picture picture;
    private Header header;
    private Page story;
    private Map<String, Object> param = new HashMap<>();

    /**
     * {@link User}.
     */
    public User getAuthor()                 { return author; }
    public void setAuthor(User author)      { this.author = author; }

    /**
     * {@link DateRange}
     */
    public void setDate(DateRange date)     { this.date = date; }
    public DateRange getDate()              { return date; }

    /**
     * {@link Category}
     */
    public Category getCategory()           { return category; }
    public void setCategory(Category category) { this.category = category; }

    /**
     * {@link Page}
     */
    public Page getPage()                   { return page; }
    public void setPage(Page page)          { this.page = page; }

    /**
     * {@link Section}
     */
    public Section getSection()             { return section; }
    public void setSection(Section section) { this.section = section; }

    /**
     * {@link Tag}
     */
    public Tag getTag()                     { return tag; }
    public void setTag(Tag tag)             { this.tag = tag; }

    /**
     * {@link Picture}
     */
    public Picture getPicture()             { return picture; }
    public void setPicture(Picture picture) { this.picture = picture; }

    /**
     * {@link Header}
     */
    public Header getHeader()               { return header; }
    public void setHeader(Header header)    { this.header = header; }

    /**
     * Story name.
     */
    public Page getStory()                  { return story; }
    public void setStory(Page story)        { this.story = story; }

    /**
     * Parameters
     */
    public Object getParam(String name)     { return param.get(name); }
    public void setParam(String name, Object value) { param.put(name, value); }

    @Override
    public Map<String, Object> getVariables()  { return param; }

    @Override
    public String getQualifier()            { return qualifier; }
    public void setQualifier(String qualifier) { this.qualifier = qualifier; }

    @Override
    public Signature getSignature() {
        Set<String> sigSet = new HashSet<>();

        for (String key : getVariables().keySet()) {
            sigSet.add('#' + key);
        }

        if (getAuthor() != null) {
            sigSet.add("author");
        }
        if (getDate() != null) {
            sigSet.add("date");
        }
        if (getCategory() != null) {
            sigSet.add("category");
        }
        if (getHeader() != null) {
            sigSet.add("header");
        }
        if (getPage() != null) {
            sigSet.add("page");
        }
        if (getPicture() != null) {
            sigSet.add("picture");
        }
        if (getSection() != null) {
            sigSet.add("section");
        }
        if (getTag() != null) {
            sigSet.add("tag");
        }
        if (getStory() != null) {
            sigSet.add("story");
        }

        return new Signature(sigSet);
   }

}
