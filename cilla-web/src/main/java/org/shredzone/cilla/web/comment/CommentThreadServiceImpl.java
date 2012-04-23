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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.CommentThread;
import org.shredzone.cilla.core.model.User;
import org.shredzone.cilla.core.model.is.Commentable;
import org.shredzone.cilla.web.format.TextFormatter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link CommentThreadService} implementation.
 *
 * @author Richard "Shred" Körber
 */
@Service
@Transactional
public class CommentThreadServiceImpl implements CommentThreadService {

    private @Resource TextFormatter textFormatter;

    @Override
    @Cacheable(value = "commentThread", key = "#commentable.thread.id")
    public CommentThreadModel getCommentThread(Commentable commentable) {
        List<CommentModel> roots = getCommentTree(commentable.getThread());

        List<CommentModel> comments = new ArrayList<CommentModel>(roots.size());
        for (CommentModel root : roots) {
            addNodeRecursive(root, comments);
        }

        Date maxDate = null;
        for (CommentModel comment : comments) {
            if (maxDate == null || comment.getCreation().after(maxDate)) {
                maxDate = comment.getCreation();
            }
        }

        CommentThreadModel result = new CommentThreadModel();
        result.setThread(comments);
        result.setCount(comments.size());
        result.setLastCommented(maxDate);
        return result;
    }

    /**
     * Adds a {@link CommentModel} and its children to the result list.
     *
     * @param node
     *            {@link CommentModel} to add
     * @param result
     *            result list to add to
     */
    private void addNodeRecursive(CommentModel node, List<CommentModel> result) {
        result.add(node);
        if (node.getChildren() != null) {
            for (CommentModel child : node.getChildren()) {
                addNodeRecursive(child, result);
            }
        }
    }

    /**
     * Get {@link CommentModel} as tree. The root comments are returned.
     *
     * @param thread
     *            {@link CommentThread}
     * @return {@link CommentModel} tree of the CommentThread's comments
     */
    private List<CommentModel> getCommentTree(CommentThread thread) {
        List<CommentModel> result = new LinkedList<CommentModel>();

        Map<Long, CommentModel> replyMap = new HashMap<Long, CommentModel>();

        for (Comment comment : thread.getComments()) {
            // Skip unpublished comments
            if (!comment.isPublished()) continue;

            CommentModel pc = assembleThreadedComment(comment);
            replyMap.put(comment.getId(), pc);

            // Make sure the comment is at least added as root if the replyTo is not found
            boolean inserted = false;

            if (comment.getReplyTo() != null) {
                long replyId = comment.getReplyTo().getId();
                CommentModel replyPc = replyMap.get(replyId);
                if (replyPc != null) {
                    pc.setLevel(replyPc.getLevel() + 1);
                    List<CommentModel> replyList = replyPc.getChildren();
                    if (replyList == null) {
                        replyList = new LinkedList<CommentModel>();
                        replyPc.setChildren(replyList);
                    }
                    replyList.add(pc);
                    inserted = true;
                }
            }

            if (!inserted) {
                result.add(pc);
            }
        }

        return result;
    }

    /**
     * Assembles a {@link CommentModel} instance for a {@link Comment}.
     *
     * @param comment
     *            {@link Comment} to assemble
     * @return Assembled {@link CommentModel}
     */
    private CommentModel assembleThreadedComment(Comment comment) {
        CommentModel pc = new CommentModel();
        pc.setId(comment.getId());
        pc.setName(comment.getName());
        pc.setUrl(comment.getUrl());
        pc.setCreation(comment.getCreation());
        pc.setText(textFormatter.format(comment.getText()).toString());

        String mail = comment.getMail();
        User creator = comment.getCreator();

        if (creator != null) {
            pc.setCreatorId(creator.getId());
            mail = creator.getMail();
        }

        if (mail != null) {
            pc.setMail(mail);
            pc.setMailhash(computeMailHash(mail));
        }

        return pc;
    }

    /**
     * Computes a mail hash that can be used at Gravatar.
     *
     * @param mail
     *            mail address
     * @return md5 hash
     */
    private static String computeMailHash(String mail) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(mail.trim().toLowerCase().getBytes("UTF-8"));

            StringBuilder digest = new StringBuilder();
            for (byte b : md5.digest()) {
                digest.append(String.format("%02x", b & 0xFF));
            }

            return digest.toString();
        } catch (NoSuchAlgorithmException ex) {
            // we expect no exception, since MD5 is a standard digester
            throw new InternalError(ex.getMessage());
        } catch (UnsupportedEncodingException ex) {
            // we expect no exception, since UTF-8 is a standard encoding
            throw new InternalError(ex.getMessage());
        }
    }

}
