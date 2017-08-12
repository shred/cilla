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
package org.shredzone.cilla.admin.comment;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.faces.model.DataModel;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.comment.CommentDto;
import org.shredzone.cilla.ws.comment.CommentWs;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * A {@link DataModel} for {@link CommentDto}. It allows partial loading and sorting.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope("view")
public class CommentDataModel extends LazyDataModel<CommentDto> {
    private static final long serialVersionUID = -1053634121748988416L;

    @Resource
    private transient CommentWs commentWs;

    @Override
    public List<CommentDto> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {
        setRowCount((int) commentWs.count());

        ListRange lr = new ListRange();
        lr.setOrder(sortField);
        lr.setDescending(sortOrder == SortOrder.DESCENDING);
        lr.setFirst(first);
        lr.setLimit(pageSize);
        return commentWs.list(lr);
    }

}
