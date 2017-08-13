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
import org.shredzone.cilla.core.model.Setting;
import org.shredzone.cilla.core.repository.SettingDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link SettingDao}.
 *
 * @author Richard "Shred" Körber
 */
@Repository("settingDao")
@Transactional
public class SettingDaoHibImpl extends BaseDaoHibImpl<Setting> implements SettingDao {

    @Override
    public Setting fetch(long id) {
        return getCurrentSession().get(Setting.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public long countAll() {
        Query<Number> q = getCurrentSession()
                .createQuery("SELECT COUNT(*) FROM Setting", Number.class);
        return q.uniqueResult().longValue();
    }

    @Override
    public List<Setting> fetchAll() {
        return getCurrentSession().createQuery("FROM Setting ORDER BY key", Setting.class).list();
    }

    @Transactional(readOnly = true)
    @Override
    public Criteria criteria() {
        return getCurrentSession().createCriteria(Setting.class);
    }

    @Override
    public Setting fetchByKey(String key) {
        return (Setting) getCurrentSession()
                .createQuery("FROM Setting WHERE key=:key")
                .setParameter("key", key)
                .uniqueResult();
    }

    @Override
    public Setting createOrUpdate(String key, String value) {
        Setting setting = fetchByKey(key);
        if (setting != null) {
            if (value != null) {
                // There is a setting, so change the value
                setting.setValue(value);
                return setting;
            } else {
                // There is a setting, so remove it
                delete(setting);
                return null;
            }
        } else {
            if (value != null) {
                // There is no setting, so create one
                setting = new Setting();
                setting.setKey(key);
                setting.setValue(value);
                persist(setting);
                return setting;
            } else {
                // The setting is already deleted, so do nothing...
                return null;
            }
        }
    }

}
