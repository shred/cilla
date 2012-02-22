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
package org.shredzone.cilla.view.model;

import java.io.Serializable;
import java.util.List;

import org.shredzone.cilla.core.model.Picture;

/**
 * Contains information about a list of pictures and a selection.
 *
 * @author Richard "Shred" Körber
 */
public class PictureInfoModel implements Serializable {
    private static final long serialVersionUID = 4310575144769950321L;

    private final List<Picture> pictureList;
    private final int selected;

    /**
     * Creates a new {@link PictureInfoModel}.
     *
     * @param pictureList
     *            List of {@link Picture}
     * @param selected
     *            Index of selected picture within that list
     */
    public PictureInfoModel(List<Picture> pictureList, int selected) {
        if (pictureList == null) {
            throw new IllegalArgumentException("pictureList must be set");
        }

        if (selected < 0 || selected >= pictureList.size()) {
            throw new IllegalArgumentException("selection out ouf pictureList");
        }

        this.pictureList = pictureList;
        this.selected = selected;
    }

    /**
     * Returns all pictures.
     */
    public List<Picture> getPictureList() {
        return pictureList;
    }

    /**
     * Returns the index of the selected picture.
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Returns the picture count, starting from 1.
     */
    public int getCount() {
        return selected + 1;
    }

    /**
     * Returns the number of pictures.
     */
    public int getNumberOfPictures() {
        return pictureList.size();
    }

    /**
     * Returns {@code true} if there are no pictures.
     */
    public boolean isEmpty() {
        return pictureList.isEmpty();
    }

    /**
     * Returns {@code true} if the first picture is selected.
     */
    public boolean isFirst() {
        return selected == 0;
    }

    /**
     * Returns {@code true} if the last picture is selected.
     */
    public boolean isLast() {
        return selected == pictureList.size() - 1;
    }

    /**
     * Returns the first {@link Picture}, or {@code null} if there are no pictures.
     */
    public Picture getFirstPicture() {
        return (!isEmpty() ? pictureList.get(0) : null);
    }

    /**
     * Returns the last {@link Picture}, or {@code null} if there are no pictures.
     */
    public Picture getLastPicture() {
        return (!isEmpty() ? pictureList.get(pictureList.size() - 1) : null);
    }

    /**
     * Returns the selected {@link Picture}.
     */
    public Picture getSelectedPicture() {
        return pictureList.get(selected);
    }

    /**
     * Returns the previous {@link Picture}, or {@code null} if the selected picture is
     * the first picture.
     */
    public Picture getPreviousPicture() {
        return (!isFirst() ? pictureList.get(selected - 1) : null);
    }

    /**
     * Returns the next {@link Picture}, or {@code null} if the selected picture is
     * the last picture.
     */
    public Picture getNextPicture() {
        return (!isLast() ? pictureList.get(selected + 1) : null);
    }

}
