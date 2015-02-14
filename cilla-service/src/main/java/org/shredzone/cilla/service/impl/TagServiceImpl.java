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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.Tag;
import org.shredzone.cilla.core.repository.PageDao;
import org.shredzone.cilla.core.repository.TagDao;
import org.shredzone.cilla.service.TagService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private @Resource PageDao pageDao;
    private @Resource TagDao tagDao;

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public List<String> proposeTags(String query, int limit) {
        return tagDao.criteria()
                .add(Restrictions.like("name", query, MatchMode.ANYWHERE))
                .setMaxResults(limit)
                .addOrder(Order.asc("name"))
                .setProjection(Projections.property("name"))
                .list();
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Cacheable("tagCloud")
    @Override
    public Map<Tag, Float> createTagCloud(long limit) {
        Date now = new Date();
        Date limitDate = new Date(now.getTime() - limit);

        List<Page> pages = pageDao.criteria()
               .add(Restrictions.and(
                   Restrictions.isNotNull("publication"),
                   Restrictions.between("publication", limitDate, now)
               ))
               .add(Restrictions.or(
                    Restrictions.isNull("expiration"),
                    Restrictions.gt("expiration", now)
               ))
               .add(Restrictions.isNotEmpty("tags"))
               .list();

        return computeTagCloud(pages, limitDate.getTime(), limit);
    }

    /**
     * Computes the tag cloud map.
     *
     * @param pages
     *            List of {@link Page} found in the time range
     * @param beginning
     *            Time range beginning (ms since epoch)
     * @param limit
     *            Time range size (ms)
     * @return Map of {@link Tag} and its cloud map factor
     */
    protected Map<Tag, Float> computeTagCloud(List<Page> pages, long beginning, long limit) {
        float max = 0f;

        Map<Tag, Float> result = new HashMap<>();

        for (Page page : pages) {
            long pageDate = page.getPublication().getTime();
            float weight = (pageDate - beginning) / (float) limit;

            for (Tag tag : page.getTags()) {
                if (result.containsKey(tag)) {
                    weight = result.get(tag) + weight;
                }
                result.put(tag, weight);
                if (weight > max) {
                    max = weight;
                }
            }
        }

        if (max != 0f) {
            for (Map.Entry<Tag, Float> entry : result.entrySet()) {
                entry.setValue(entry.getValue() / max);
            }
        }

        return result;
    }

}
