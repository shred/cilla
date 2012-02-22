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
package org.shredzone.cilla.service.search.strategy;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.impl.SearchResultImpl;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.stereotype.Component;

/**
 * A simple {@link SearchStrategy} implementation. This strategy is used when no query
 * string is set in the {@link FilterModel}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class SimpleSearchStrategy extends AbstractSearchStrategy {

    @Override
    public void count(SearchResultImpl result) throws CillaServiceException {
        Criteria crit = createCriteria(result);
        crit.setProjection(Projections.rowCount());
        result.setCount(((Number) crit.uniqueResult()).intValue());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void search(SearchResultImpl result) throws CillaServiceException {
        Criteria crit = createPaginatedCriteria(result);
        result.setPages(crit.list());
        result.setHighlighted(null);
    }

}
