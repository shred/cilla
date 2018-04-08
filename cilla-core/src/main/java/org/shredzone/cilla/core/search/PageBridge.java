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
package org.shredzone.cilla.core.search;

import static java.util.stream.Collectors.joining;

import java.util.Objects;

import org.hibernate.search.bridge.StringBridge;
import org.shredzone.cilla.core.model.GallerySection;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.model.Section;
import org.shredzone.cilla.core.model.TextSection;

/**
 * A {@link StringBridge} that converts the contents of an entire {@link Page} to a
 * String. All the {@link Page}'s {@link Section} and contents (like {@link Picture}) are
 * taken into account as well. The result is a raw text without any markup.
 *
 * @author Richard "Shred" Körber
 */
public class PageBridge implements StringBridge {

    @Override
    public String objectToString(Object obj) {
        if (obj == null) {
            return null;
        }

        if (!(obj instanceof Page)) {
            throw new IllegalArgumentException("Unknown object type, cannot convert: " + obj.getClass());
        }

        Page page = (Page) obj;

        StringBuilder sb = new StringBuilder();

        sb.append(PlainTextFormatter.format(page.getTeaser()));
        sb.append(' ');
        sb.append(page.getSections().stream()
                .map(this::formatSection)
                .collect(joining(" ")));

        return sb.toString();
    }

    private CharSequence formatSection(Section section) {
        if (section instanceof TextSection) {
            return PlainTextFormatter.format(((TextSection) section).getText());

        } else if (section instanceof GallerySection) {
            GallerySection gs = (GallerySection) section;
            return gs.getPictures().stream()
                    .map(pic -> PlainTextFormatter.format(pic.getCaption()))
                    .filter(Objects::nonNull)
                    .collect(joining(" "));

        } else {
            return "";
        }
    }

}
