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
package org.shredzone.cilla.admin.page.media;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.DataModel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.shredzone.cilla.ws.page.MediumDto;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A {@link DataModel} for page media. It supports partial loading, but no sorting.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("view")
public class MediaDataModel extends LazyDataModel<MediumDto> {
    private static final long serialVersionUID = 76347037561090724L;

    private @Resource PageMediaBean pageMediaBean;

    @Override
    public List<MediumDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        List<MediumDto> mediaList = pageMediaBean.getMedia();
        setRowCount(mediaList.size());
        int max = Math.min(first + pageSize, mediaList.size());
        return mediaList.subList(first, max);
    }

    @Override
    public Object getRowKey(MediumDto object) {
        return pageMediaBean.getMedia().indexOf(object);
    }

    @Override
    public MediumDto getRowData(String rowKey) {
        return pageMediaBean.getMedia().get(Integer.parseInt(rowKey));
    }

}
