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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A notification is a message that is sent to different receivers.
 *
 * @author Richard "Shred" Körber
 */
public class Notification {

    private String type;
    private List<NotificationTarget> recipients = new ArrayList<NotificationTarget>();
    private Map<String, Object> attributes = new HashMap<String, Object>();

    /**
     * Notification type. This is the template name to be used.
     */
    public String getType()                 { return type; }
    public void setType(String type)        { this.type = type; }

    /**
     * List of recipients.
     */
    public List<NotificationTarget> getRecipients() { return recipients; }
    public void setRecipients(List<NotificationTarget> recipients) { this.recipients = recipients; }

    /**
     * Attributes for placeholders in the message.
     */
    public Map<String, Object> getAttributes() { return attributes; }
    public void setAttributes(Map<String, Object> attributes) { this.attributes = attributes; }

}
