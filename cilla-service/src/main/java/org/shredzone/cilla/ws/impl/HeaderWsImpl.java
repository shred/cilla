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

import javax.activation.DataHandler;
import javax.annotation.Resource;
import javax.jws.WebService;

import org.hibernate.Criteria;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.shredzone.cilla.core.model.Header;
import org.shredzone.cilla.core.repository.HeaderDao;
import org.shredzone.cilla.service.HeaderService;
import org.shredzone.cilla.ws.AbstractWs;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.ListRange;
import org.shredzone.cilla.ws.assembler.HeaderAssembler;
import org.shredzone.cilla.ws.exception.CillaNotFoundException;
import org.shredzone.cilla.ws.exception.CillaParameterException;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.shredzone.cilla.ws.header.HeaderDto;
import org.shredzone.cilla.ws.header.HeaderWs;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link HeaderWs}.
 *
 * @author Richard "Shred" Körber
 */
@Transactional
@Secured("ROLE_WEBSERVICE")
@Service
@WebService(serviceName = "HeaderWs",
    endpointInterface = "org.shredzone.cilla.ws.header.HeaderWs",
    targetNamespace = "http://ws.cilla.shredzone.org/")
public class HeaderWsImpl extends AbstractWs implements HeaderWs {

    private @Resource HeaderAssembler headerAssembler;
    private @Resource HeaderDao headerDao;
    private @Resource HeaderService headerService;

    @Override
    public HeaderDto fetch(long id) throws CillaServiceException {
        Header header = headerDao.fetch(id);
        return (header != null ? headerAssembler.assemble(header) : null);
    }

    @Override
    public long count() {
        return headerDao.countAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<HeaderDto> list(ListRange lr) {
        Criteria crit = headerDao.criteria()
            .createAlias("creator", "c")
            .setProjection(headerAssembler.projection())
            .setResultTransformer(new AliasToBeanResultTransformer(HeaderDto.class));

        applyListRange(lr, "creation", true, crit);

        return crit.list();
    }

    @Override
    public HeaderDto createNew() throws CillaServiceException {
        return headerAssembler.assemble(headerService.createNew());
    }

    @Override
    public HeaderDto commit(HeaderDto header) throws CillaServiceException {
        if (header.isPersisted()) {
            Header entity = headerDao.fetch(header.getId());
            headerAssembler.merge(header, entity);
            headerService.update(entity);

            if (header.getUploadFullFile() != null || header.getUploadHeaderFile() != null) {
                headerService.updateImage(
                    entity,
                    (header.getUploadHeaderFile() != null ? header.getUploadHeaderFile().getDataSource() : null),
                    (header.getUploadFullFile() != null ? header.getUploadFullFile().getDataSource() : null)
                );
            }

        } else {
            if (header.getUploadFullFile() == null || header.getUploadHeaderFile() == null) {
                throw new CillaParameterException("header and full-view file must be set for new header");
            }

            Header entity = new Header();
            headerAssembler.merge(header, entity);
            headerService.create(
                entity,
                header.getUploadHeaderFile().getDataSource(),
                header.getUploadFullFile().getDataSource()
            );
            header.setId(entity.getId());
        }

        return header;
    }

    @Override
    public void delete(long headerId) throws CillaServiceException {
        headerService.remove(headerDao.fetch(headerId));
    }

    @Override
    public DataHandler getHeaderImage(long headerId, ImageProcessing process) throws CillaServiceException {
        Header hdr = headerDao.fetch(headerId);
        if (hdr == null) {
            throw new CillaNotFoundException("header", headerId);
        }

        return new DataHandler(headerService.getHeaderImage(hdr, process));
    }

    @Override
    public DataHandler getFullImage(long headerId, ImageProcessing process) throws CillaServiceException {
        Header hdr = headerDao.fetch(headerId);
        if (hdr == null) {
            throw new CillaNotFoundException("header", headerId);
        }

        return new DataHandler(headerService.getFullImage(hdr, process));
    }

}
