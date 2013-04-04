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

import java.io.IOException;
import java.io.StringWriter;

import javax.annotation.Resource;

import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * Default implementation of {@link NotificationService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Value("${notification.sender}") String sender;

    private @Resource Configuration freemarkerConfiguration;
    private @Resource MailSender mailSender;

    @Override
    public void send(Notification notification) throws CillaServiceException {
        for (NotificationTarget target : notification.getRecipients()) {
            if (target.getMail() != null) {
                sendToTarget(target, notification);
            }
        }
    }

    /**
     * Send a {@link Notification} to a single {@link NotificationTarget}.
     * <p>
     * For the receiver's locale, the appropriate template is opened, it's placeholders
     * filled with the notification parameters, and the message sent to the target's
     * mail address.
     *
     * @param target
     *            {@link NotificationTarget} to send to
     * @param notification
     *            {@link Notification} to send
     */
    private void sendToTarget(NotificationTarget target, Notification notification)
    throws CillaServiceException {
        try {
            Template template = freemarkerConfiguration.getTemplate(
                    notification.getType() + ".ftl",
                    target.getLocale()
            );

            StringWriter out = new StringWriter();
            Environment env = template.createProcessingEnvironment(notification.getAttributes(), out, null);
            env.process();
            out.close();

            TemplateModel subjectTm = env.getVariable("subject");

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom(sender);
            msg.setTo(target.getMail());
            msg.setSubject(subjectTm != null ? subjectTm.toString() : "");
            msg.setText(out.toString());
            mailSender.send(msg);

        } catch (IOException | TemplateException ex) {
            log.error("Failed to process template " , ex);
            throw new CillaServiceException("Could not process template", ex);
        }
    }

}
