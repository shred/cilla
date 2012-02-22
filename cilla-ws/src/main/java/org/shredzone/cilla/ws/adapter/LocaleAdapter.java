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
package org.shredzone.cilla.ws.adapter;

import java.util.Locale;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for transporting {@link Locale} instances via XML.
 *
 * @author Richard "Shred" Körber
 */
public class LocaleAdapter extends XmlAdapter<String, Locale> {

    @Override
    public Locale unmarshal(String str) throws Exception {
        if (str == null) {
            return null;
        }

        String[] parts = str.split("_", 3);
        if (parts.length == 3) {
            return new Locale(parts[0], parts[1], parts[2]);
        } else if (parts.length == 2) {
            return new Locale(parts[0], parts[1]);
        } else {
            return new Locale(parts[0]);
        }
    }

    @Override
    public String marshal(Locale locale) throws Exception {
        return (locale != null ? locale.toString() : null);
    }

}
