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
package org.shredzone.cilla.ws.category;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.TextFormat;

/**
 * A Category.
 *
 * @author Richard "Shred" Körber
 */
public class CategoryDto extends BaseDto {
    private static final long serialVersionUID = 1435967564486469262L;

    private String name;
    private String title;
    private String icon;
    private String caption;
    private TextFormat captionFormat;

    @NotNull
    @Size(min = 1, max = 255)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    @Size(max = 255)
    public String getTitle()                    { return title; }
    public void setTitle(String title)          { this.title = title; }

    @Size(max = 255)
    public String getIcon()                     { return icon; }
    public void setIcon(String icon)            { this.icon = icon; }

    public String getCaption()                  { return caption; }
    public void setCaption(String caption)      { this.caption = caption; }

    @NotNull
    public TextFormat getCaptionFormat()        { return captionFormat; }
    public void setCaptionFormat(TextFormat captionFormat) { this.captionFormat = captionFormat; }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof CategoryDto && super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode() ^ super.hashCode();
    }

}
