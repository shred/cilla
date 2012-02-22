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
package org.shredzone.cilla.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.repository.CommentDao;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.service.CommentService;
import org.shredzone.cilla.service.ConfigurationService;
import org.shredzone.cilla.service.SecurityService;
import org.shredzone.cilla.service.notification.Notification;
import org.shredzone.cilla.service.notification.NotificationService;
import org.shredzone.cilla.service.notification.NotificationTarget;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link CommentService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private static final Pattern URL_PATTERN = Pattern.compile("[a-zA-Z]+\\:\\/\\/.*");

    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource UserDao userDao;
    private @Resource CommentDao commentDao;
    private @Resource SecurityService securityService;
    private @Resource NotificationService notificationService;
    private @Resource ConfigurationService configurationService;

    @Override
    public Comment createNew(Page page) {
        Comment comment = new Comment();
        comment.setText(new FormattedText());
        comment.setCreation(new Date());
        comment.setPublished(false);
        comment.setPage(page);
        comment.setCreator(userDao.fetch(securityService.getAuthenticatedUser().getUserId()));
        return comment;
    }

    @Override
    public void postSilently(Comment comment) {
        postComment(comment, null);
    }

    @Override
    public void postComment(Comment comment, Object request) {
        if (!configurationService.get("comment.masterenabled", Boolean.TRUE)) {
            // Comments are globally disabled.
            return;
        }

        Date now = new Date();

        normalizeUrl(comment);

        comment.setCreation(now);
        comment.setPublished(true);
        comment.getText().setFormat(configurationService.get("comment.anonymous.format", TextFormat.PLAIN));

        commentDao.persist(comment);

        List<NotificationTarget> targets = new ArrayList<NotificationTarget>();
        for (User user : userDao.fetchAllWithAuthority("MODERATOR")) {
            NotificationTarget target = new NotificationTarget();
            target.setLocale(user.getLanguage().getLocale());
            target.setMail(user.getMail());
            target.setName(user.getName());
            targets.add(target);
        }

        try {
            Notification notification = new Notification();
            notification.setType("cillaCommentAdded");
            notification.setRecipients(targets);
            notification.getAttributes().put("comment", comment);
            notification.getAttributes().put("page", comment.getPage());
            notification.getAttributes().put("request", request);
            notificationService.send(notification);
        } catch (CillaServiceException ex) {
            log.error("Could not send notification for comment id " + comment.getId(), ex);
        }
    }

    @Override
    public void remove(Comment comment) {
        securityService.requireRole("ROLE_MODERATOR");
        for (Comment reply : commentDao.fetchReplies(comment)) {
            remove(reply);
        }
        commentDao.delete(comment);
    }

    @Override
    public void removeAll(Page page) {
        for (Comment comment : commentDao.fetchComments(page)) {
            commentDao.delete(comment);
        }
    }

    /**
     * Normalizes the commentor's URL, making sure that it is at least not relative to
     * this blog.
     *
     * @param comment
     *            {@link Comment} to validate
     */
    private void normalizeUrl(Comment comment) {
        String url = comment.getUrl();

        if (url == null || url.isEmpty()) {
            // There is no URL set
            return;
        }

        if (URL_PATTERN.matcher(url).matches()) {
            // It seems to be a valid URL
            return;
        }

        // Prepend a "http://" so the URL is valid (or at least not relative to the blog's URL)
        comment.setUrl("http://" + url);
    }

}
