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
package org.shredzone.cilla.ws.page;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElementRef;

import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.cilla.ws.category.CategoryDto;

/**
 * A Page with more information about a page. {@link PageDto} instances may become quite
 * large.
 *
 * @author Richard "Shred" Körber
 */
public class PageDto extends PageInfoDto {
    private static final long serialVersionUID = 4119860087137058746L;

    private String teaser;
    private TextFormat teaserFormat;
    private String challenge;
    private String responsePattern;
    private List<SectionDto> sections = new ArrayList<>();
    private List<MediumDto> media = new ArrayList<>();
    private List<CategoryDto> categories = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    @NotNull
    public String getTeaser()                       { return teaser; }
    public void setTeaser(String teaser)            { this.teaser = teaser; }

    @NotNull
    public TextFormat getTeaserFormat()             { return teaserFormat; }
    public void setTeaserFormat(TextFormat teaserFormat) { this.teaserFormat = teaserFormat; }

    @Size(min = 0, max = 255)
    public String getChallenge()                    { return challenge; }
    public void setChallenge(String challenge)      { this.challenge = challenge; }

    @Size(min = 0, max = 255)
    public String getResponsePattern()              { return responsePattern; }
    public void setResponsePattern(String responsePattern) { this.responsePattern = responsePattern; }

    @XmlElementRef
    public List<SectionDto> getSections()           { return sections; }
    public void setSections(List<SectionDto> sections) { this.sections = sections; }

    public List<MediumDto> getMedia()                { return media; }
    public void setMedia(List<MediumDto> media)      { this.media = media; }

    public List<CategoryDto> getCategories()        { return categories; }
    public void setCategories(List<CategoryDto> categories) { this.categories = categories; }

    public List<String> getTags()                   { return tags; }
    public void setTags(List<String> tags)          { this.tags = tags; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof PageDto && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
