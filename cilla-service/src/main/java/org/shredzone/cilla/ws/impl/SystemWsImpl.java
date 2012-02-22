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
import org.shredzone.cilla.core.repository.LanguageDao;
import org.shredzone.cilla.ws.assembler.LanguageAssembler;
import org.shredzone.cilla.ws.system.LanguageDto;
import org.shredzone.cilla.ws.system.SystemWs;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link SystemWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "SystemWs",
    endpointInterface = "org.shredzone.cilla.ws.system.SystemWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class SystemWsImpl implements SystemWs {

    private @Resource LanguageAssembler languageAssembler;
    private @Resource LanguageDao languageDao;

    @SuppressWarnings("unchecked")
    @Override
    public List<LanguageDto> listLanguages() {
        return languageDao.criteria()
            .setProjection(languageAssembler.projection())
            .addOrder(Order.asc("name"))
            .setResultTransformer(new AliasToBeanResultTransformer(LanguageDto.class))
            .list();
    }

}
