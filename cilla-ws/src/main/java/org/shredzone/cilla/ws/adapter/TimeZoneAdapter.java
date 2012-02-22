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

import java.util.TimeZone;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Adapter for transporting {@link TimeZone} instances via XML.
 *
 * @author Richard "Shred" Körber
 */
public class TimeZoneAdapter extends XmlAdapter<String, TimeZone> {

    @Override
    public TimeZone unmarshal(String str) throws Exception {
        return (str != null ? TimeZone.getTimeZone(str) : null);
    }

    @Override
    public String marshal(TimeZone locale) throws Exception {
        return (locale != null ? locale.getID() : null);
    }

}
