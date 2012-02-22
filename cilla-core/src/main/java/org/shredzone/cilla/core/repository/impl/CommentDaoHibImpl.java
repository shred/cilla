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
package org.shredzone.cilla.core.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.shredzone.cilla.core.model.Comment;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.repository.CommentDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link CommentDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("commentDao")
@Transactional
public class CommentDaoHibImpl extends BaseDaoHibImpl<Comment> implements CommentDao {

    @Override
    public void persist(Comment data) {
        super.persist(data);
    }

    @Transactional(readOnly = true)
    @Override
    public Comment fetch(long id) {
        return (Comment) getCurrentSession().get(Comment.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Comment.class);
    }

    @Override
    public long countAll() {
        Query q = getCurrentSession()
                        .getNamedQuery("countAllComments");
        return ((Number) q.uniqueResult()).longValue();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> fetchAll() {
        return getCurrentSession().createQuery("FROM Comment ORDER BY creation").list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> fetchReplies(Comment comment) {
        return getCurrentSession()
                .createQuery("FROM Comment WHERE replyTo=:reply ORDER BY creation")
                .setParameter("reply", comment)
                .list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> fetchPublishedComments(Page page) {
        return getCurrentSession()
                .createQuery("FROM Comment" +
                        " WHERE page=:page AND sectionRef IS NULL AND published=true" +
                        " ORDER BY creation")
        .setParameter("page", page)
        .list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Comment> fetchComments(Page page) {
        return getCurrentSession()
                .createQuery("FROM Comment WHERE page=:page ORDER BY creation")
        .setParameter("page", page)
        .list();
    }

}
