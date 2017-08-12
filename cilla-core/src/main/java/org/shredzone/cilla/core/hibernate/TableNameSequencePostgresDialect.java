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
package org.shredzone.cilla.core.hibernate;

import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * A {@link PostgreSQL82Dialect} that generates an individual sequence for every table,
 * instead of sharing a single sequence to all tables.
 *
 * @author Richard "Shred" Körber
 * @see <a href="http://community.jboss.org/wiki/Customsequences">http://community.jboss.org/wiki/Customsequences</a>
 */
public class TableNameSequencePostgresDialect extends PostgreSQL82Dialect {

    @Override
    public Class<?> getNativeIdentifierGeneratorClass() {
        return TableNameSequenceGenerator.class;
    }

    public static class TableNameSequenceGenerator extends SequenceStyleGenerator {

        @Override
        public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
            if (params.getProperty(SEQUENCE_PARAM) == null || params.getProperty(SEQUENCE_PARAM).isEmpty()) {
                String tableName = params.getProperty(PersistentIdentifierGenerator.TABLE);
                if (tableName != null) {
                    params.setProperty(SEQUENCE_PARAM, "seq_" + tableName);
                }
            }

            super.configure(type, params, serviceRegistry);
        }

    }

}
