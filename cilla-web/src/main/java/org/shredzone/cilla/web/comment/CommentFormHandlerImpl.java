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
package org.shredzone.cilla.web.comment;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.CommentThread;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.core.repository.CommentDao;
import org.shredzone.cilla.core.repository.UserDao;
import org.shredzone.cilla.service.CommentService;
import org.shredzone.cilla.service.SecurityService;
import org.shredzone.cilla.service.security.CillaUserDetails;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.captcha.CaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link CommentFormHandler}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class CommentFormHandlerImpl implements CommentFormHandler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Value("${comment.maxlength}") int maxCommentLength;

    private @Resource UserDao userDao;
    private @Resource CommentDao commentDao;
    private @Resource CaptchaService captchaService;
    private @Resource CommentService commentService;
    private @Resource SecurityService securityService;

    @Override
    @CacheEvict(value = "commentThread", key = "#commentable.thread.id")
    public void handleComment(Commentable commentable, HttpServletRequest req) {
        handleComment(commentable, req, true);
    }

    @Override
    @CacheEvict(value = "commentThread", key = "#commentable.thread.id")
    public void handleComment(Commentable commentable, HttpServletRequest req, boolean enabled) {
        CommentThread thread = commentable.getThread();

        handleDelete(thread, req);

        Comment comment = new Comment();

        if (req.getParameter("cmtPosted") == null) {
            // There is no comment posting to be handled
            return;
        }

        if (!(thread.isCommentable() && enabled)) {
            // The thread does not accept new comments
            log.info("Trying to comment thread ID {} which does not allow comments.", thread.getId());
            return;
        }

        String commentText = getMultiline(req.getParameter("cmtComment"));
        if (commentText.isEmpty()) {
            // There was no comment text
            rejectComment(req, "cilla.comment.error.empty");
            return;
        }
        if (commentText.length() > maxCommentLength) {
            rejectComment(req, "cilla.comment.error.toolong");
            return;
        }

        CillaUserDetails cud = securityService.getAuthenticatedUser();
        if (cud != null) {
            User user = userDao.fetch(cud.getUserId());
            comment.setCreator(user);
            comment.setMail(user.getMail());
            comment.setName(user.getName());
        } else {
            String name = getSimple(req.getParameter("cmtName"));
            String mail = getSimple(req.getParameter("cmtMail"));
            String url = getSimple(req.getParameter("cmtUrl"));

            if (userDao.fetchByLogin(name) != null) {
                rejectComment(req, "cilla.comment.error.badname");
                return;
            }

            comment.setMail(mail);
            comment.setName(name);
            comment.setUrl(url);
        }

        int posX = getReplyToInteger(req.getParameter("x"));
        int posY = getReplyToInteger(req.getParameter("y"));
        if (!captchaService.isValidCaptcha(req.getSession(), posX, posY)) {
            // Wrong captcha given, do not accept the comment.
            log.info("A wrong captcha was given for thread ID {}", thread.getId());
            rejectComment(req, "cilla.comment.error.badcaptcha");
            return;
        }

        long replyToId = getReplyToId(req.getParameter("cmtReplyTo"));
        Comment replyComment = commentDao.fetch(replyToId);
        if (replyComment != null && !replyComment.getThread().equals(thread)) {
            // The Comment belongs to another thread, so the replyToId was
            // probably forged. We will silently ignore the replyTo comment.
            log.info("Ignored forged replyToId {} for thread ID {}", replyToId, thread.getId());
            replyComment = null;
        }

        // Comment looks good...

        comment.setThread(thread);
        comment.setReplyTo(replyComment);
        comment.setText(new FormattedText(commentText, TextFormat.PLAIN));

        commentService.postComment(comment, req);

        if (comment.isPublished()) {
            setMessage(req, "cilla.comment.ispublished");
        } else {
            setMessage(req, "cilla.comment.ismoderated");
        }
    }

    /**
     * Handles a delete request.
     *
     * @param thread
     *            {@link CommentThread} the comment belongs to
     * @param req
     *            {@link HttpServletRequest} with the form data
     */
    private void handleDelete(CommentThread thread, HttpServletRequest req) {
        String delId = req.getParameter("cmtDelete");
        if (delId != null) {
            long deleteId = getReplyToId(delId);
            Comment deleteComment = commentDao.fetch(deleteId);
            if (deleteComment != null && deleteComment.getThread().equals(thread)) {
                commentService.remove(deleteComment);
            }
        }
    }

    /**
     * Rejects a comment. When the {@link Page} is shown again, an error message is shown
     * and the form contains the previous values.
     *
     * @param req
     *            {@link HttpServletRequest} with the form data
     * @param message
     *            the error message to be shown
     */
    private void rejectComment(HttpServletRequest req, String message) {
        setMessage(req, message);
        req.setAttribute("commentReplyTo", req.getParameter("cmtReplyTo"));
        req.setAttribute("commentName", req.getParameter("cmtName"));
        req.setAttribute("commentMail", req.getParameter("cmtMail"));
        req.setAttribute("commentUrl", req.getParameter("cmtUrl"));
        req.setAttribute("commentComment", req.getParameter("cmtComment"));
    }

    /**
     * Sets a message to be shown when the {@link Page} is rendered again.
     *
     * @param req
     *            {@link HttpServletRequest} with the form data
     * @param message
     *            the error message to be shown
     */
    private void setMessage(HttpServletRequest req, String message) {
        req.setAttribute("commentMessageKey", message);
    }

    /**
     * Returns a parameter as integer value.
     *
     * @param value
     *            the value string to be converted
     * @return integer value, or -1 if the {@link String} could not be parsed
     */
    private int getReplyToInteger(String value) {
        if (value == null || value.isEmpty()) {
            return -1;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Returns a parameter as long value to be used for database ids.
     *
     * @param value
     *            the value string to be converted
     * @return long value, or 0 if the {@link String} could not be parsed
     */
    private long getReplyToId(String value) {
        if (value == null || value.isEmpty()) {
            return 0;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * Returns a simple string. Newline characters are replaced by spaces, leading and
     * trailing spaces are trimmed, and the string length is limited to 255 characters.
     *
     * @param value
     *            the value string to be converted
     * @return simplified string
     */
    private String getSimple(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        String result = value.replaceAll("[\\r\\n]", " ").trim();
        if (result.length() > 255) {
            result = result.substring(0, 255);
        }
        return result;
    }

    /**
     * Returns a multi line string. Leading and trailing spaces are trimmed.
     *
     * @param value
     *            the value string to be converted
     * @return multiline string
     */
    private String getMultiline(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }

        return value.trim();
    }

}
