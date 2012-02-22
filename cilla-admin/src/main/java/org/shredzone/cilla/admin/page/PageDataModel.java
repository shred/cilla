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
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.DataModel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.page.PageInfoDto;
import org.shredzone.cilla.ws.page.PageWs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A {@link DataModel} providing {@link PageInfoDto}. It supports partial loading and
 * sorting.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("view")
public class PageDataModel extends LazyDataModel<PageInfoDto> {
    private static final long serialVersionUID = -6563621904935828762L;

    private @Resource PageWs pageWs;

    @Override
    public List<PageInfoDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        setRowCount((int) pageWs.count());

        ListRange lr = new ListRange();
        lr.setOrder(sortField);
        lr.setDescending(sortOrder == SortOrder.DESCENDING);
        lr.setFirst(first);
        lr.setLimit(pageSize);
        return pageWs.list(lr);
    }

}
