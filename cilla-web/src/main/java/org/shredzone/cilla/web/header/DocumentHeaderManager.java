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
package org.shredzone.cilla.web.header;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Manages {@link DocumentHeader} instances.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class DocumentHeaderManager {

    private @Resource ApplicationContext applicationContext;

    /**
     * Gets the {@link DocumentHeader} for the current HTTP request.
     * <p>
     * Request scoped beans may also inject the {@link DocumentHeader} directly.
     *
     * @return {@link DocumentHeader}
     */
    public DocumentHeader getDocumentHeader() {
        DocumentHeader header = applicationContext.getBean(DocumentHeader.class);
        if (!header.isSetup()) {
            for (DocumentHeaderObserver observer : applicationContext.getBeansOfType(DocumentHeaderObserver.class).values()) {
                observer.onNewDocumentHeader(header);
            }
            header.setup();
        }
        return header;
    }

}
