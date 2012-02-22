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
package org.shredzone.cilla.ws.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebService;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.shredzone.cilla.core.repository.TagDao;
import org.shredzone.cilla.service.TagService;
import org.shredzone.cilla.ws.tag.TagWs;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link TagWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "TagWs",
    endpointInterface = "org.shredzone.cilla.ws.tag.TagWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class TagWsImpl implements TagWs {

    private @Resource TagDao tagDao;
    private @Resource TagService tagService;

    @Override
    public long count() {
        return tagDao.countAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> list() {
        return tagDao.criteria()
                .addOrder(Order.asc("name"))
                .setProjection(Projections.property("name"))
                .list();
    }

    @Override
    public List<String> proposeTags(String query, int limit) {
        return tagService.proposeTags(query, limit);
    }

}
