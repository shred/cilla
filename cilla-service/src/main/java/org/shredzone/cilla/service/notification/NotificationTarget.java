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
package org.shredzone.cilla.service.notification;

import java.util.Locale;

/**
 * A target to send a notification to.
 *
 * @author Richard "Shred" Körber
 */
public class NotificationTarget {

    private String name;
    private String mail;
    private Locale locale;

    /**
     * The receiver's name.
     */
    public String getName()                 { return name; }
    public void setName(String name)        { this.name = name; }

    /**
     * The receiver's mail address to send the notification to.
     */
    public String getMail()                 { return mail; }
    public void setMail(String mail)        { this.mail = mail; }

    /**
     * The receiver's language.
     */
    public Locale getLocale()               { return locale; }
    public void setLocale(Locale locale)    { this.locale = locale; }

}
