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
import org.hibernate.query.Query;
import org.shredzone.cilla.core.model.Picture;
import org.shredzone.cilla.core.repository.PictureDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link PictureDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("pictureDao")
@Transactional
public class PictureDaoHibImpl extends BaseDaoHibImpl<Picture> implements PictureDao {

    @Transactional(readOnly = true)
    @Override
    public Picture fetch(long id) {
        return getCurrentSession().get(Picture.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Picture", Number.class);
        return q.uniqueResult().longValue();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Picture> fetchAll() {
        return getCurrentSession()
                .createQuery("FROM Picture")
                .list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Picture.class);
    }

}
