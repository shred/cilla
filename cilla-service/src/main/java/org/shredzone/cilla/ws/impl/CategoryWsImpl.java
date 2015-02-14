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
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.shredzone.cilla.core.model.Category;
import org.shredzone.cilla.core.repository.CategoryDao;
import org.shredzone.cilla.service.CategoryService;
import org.shredzone.cilla.ws.assembler.CategoryAssembler;
import org.shredzone.cilla.ws.category.CategoryDto;
import org.shredzone.cilla.ws.category.CategoryWs;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link CategoryWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "CategoryWs",
    endpointInterface = "org.shredzone.cilla.ws.category.CategoryWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class CategoryWsImpl implements CategoryWs {

    private @Resource CategoryDao categoryDao;
    private @Resource CategoryService categoryService;
    private @Resource CategoryAssembler categoryAssembler;

    @Override
    public CategoryDto fetch(long id) throws CillaServiceException {
        Category cat = categoryDao.fetch(id);
        return (cat != null ? categoryAssembler.assemble(cat) : null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoryDto> list() throws CillaServiceException {
        return categoryDao.criteria()
                .setProjection(categoryAssembler.projection())
                .addOrder(Order.asc("name"))
                .setResultTransformer(new AliasToBeanResultTransformer(CategoryDto.class))
                .list();
    }

    @Override
    public CategoryDto createNew() throws CillaServiceException {
        return categoryAssembler.assemble(categoryService.createNew());
    }

}
