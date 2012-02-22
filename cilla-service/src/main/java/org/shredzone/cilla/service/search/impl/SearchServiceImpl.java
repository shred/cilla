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
package org.shredzone.cilla.service.search.impl;

import javax.annotation.Resource;

import org.shredzone.cilla.service.search.FilterModel;
import org.shredzone.cilla.service.search.SearchResult;
import org.shredzone.cilla.service.search.SearchService;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link SearchService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {

    private @Resource ApplicationContext applicationContext;

    @Override
    public SearchResult search(FilterModel filter) throws CillaServiceException {
        SearchResult result = applicationContext.getBean(SearchResult.class);
        result.setFilter(filter);
        return result;
    }

}
