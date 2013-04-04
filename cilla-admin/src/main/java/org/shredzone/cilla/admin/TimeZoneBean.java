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
package org.shredzone.cilla.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import org.springframework.stereotype.Component;

/**
 * A bean helping to select a time zone. When the user started to type, it shows potential
 * matches.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class TimeZoneBean implements Converter {

    private final SortedMap<String, TimeZone> timeZoneMap = new TreeMap<>();
    private int maxResults = 10;

    @PostConstruct
    public void setup() {
        for (String id : TimeZone.getAvailableIDs()) {
            TimeZone tz = TimeZone.getTimeZone(id);
            timeZoneMap.put(id, tz);
            timeZoneMap.put(tz.getDisplayName(Locale.ENGLISH).toLowerCase(), tz);
        }
    }

    /**
     * Returns a list of proposals for the given query string.
     *
     * @param query
     *            Query string
     * @return List of proposed time zones matching the string
     */
    public List<String> complete(String query) {
        String qstr = query.trim().toLowerCase();
        List<String> result = new ArrayList<>(maxResults);

        for (String entry : timeZoneMap.keySet()) {
            if (entry.toLowerCase().contains(qstr)) {
                result.add(entry);
                if (result.size() == maxResults) {
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String string) {
        try {
            if (string == null) {
                return null;
            }

            String qstr = string.trim();

            if (qstr.isEmpty()) {
                return null;
            }

            if (timeZoneMap.containsKey(qstr)) {
                return timeZoneMap.get(qstr);
            }

            for (Map.Entry<String, TimeZone> entry : timeZoneMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(qstr)) {
                    return entry.getValue();
                }
            }

            return TimeZone.getTimeZone(string);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ConverterException(ex);
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        try {
            TimeZone tz = (TimeZone) o;
            return tz.getID();
        } catch (Exception ex) {
            throw new ConverterException(ex);
        }
    }

}
