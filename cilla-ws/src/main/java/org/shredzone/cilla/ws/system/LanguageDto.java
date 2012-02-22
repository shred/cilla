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
package org.shredzone.cilla.ws.system;

import java.util.Locale;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.shredzone.cilla.ws.BaseDto;
import org.shredzone.cilla.ws.adapter.LocaleAdapter;

/**
 * A Language.
 *
 * @author Richard "Shred" Körber
 */
public class LanguageDto extends BaseDto {
    private static final long serialVersionUID = 364211637929908477L;

    private String name;
    private Locale locale;

    @NotNull
    @Size(min = 1, max = 255)
    public String getName()                     { return name; }
    public void setName(String name)            { this.name = name; }

    @NotNull
    @XmlJavaTypeAdapter(LocaleAdapter.class)
    public Locale getLocale()                   { return locale; }
    public void setLocale(Locale locale)        { this.locale = locale; }

}
