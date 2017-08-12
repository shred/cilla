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
package org.shredzone.cilla.admin.header;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.DataModel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.header.HeaderDto;
import org.shredzone.cilla.ws.header.HeaderWs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A {@link DataModel} providing {@link HeaderDto}. It supports partial loading and
 * sorting.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("view")
public class HeaderDataModel extends LazyDataModel<HeaderDto> {
    private static final long serialVersionUID = 6396579014639216274L;

    @Resource
    private transient HeaderWs headerWs;

    @Override
    public List<HeaderDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        setRowCount((int) headerWs.count());

        ListRange lr = new ListRange();
        lr.setOrder(sortField);
        lr.setDescending(sortOrder == SortOrder.DESCENDING);
        lr.setFirst(first);
        lr.setLimit(pageSize);
        return headerWs.list(lr);
    }

}
