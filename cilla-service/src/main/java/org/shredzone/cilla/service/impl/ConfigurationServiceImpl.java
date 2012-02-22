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

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Setting;
import org.shredzone.cilla.core.repository.SettingDao;
import org.shredzone.cilla.service.ConfigurationService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link ConfigurationService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
@Transactional
public class ConfigurationServiceImpl implements ConfigurationService {

    private @Resource SettingDao settingDao;
    private @Resource ConversionService conversionService;

    @Override
    public boolean hasKey(String key) {
        Setting setting = settingDao.fetchByKey(key);
        return (setting != null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, T def) {
        Setting setting = settingDao.fetchByKey(key);

        if (setting == null) {
            return def;
        }

        return (T) conversionService.convert(setting.getValue(), def.getClass());
    }

    @Override
    public <T> void put(String key, T value) {
        String strValue = conversionService.convert(value, String.class);
        settingDao.createOrUpdate(key, strValue);
    }

    @Override
    public void remove(String key) {
        Setting setting = settingDao.fetchByKey(key);
        if (setting != null) {
            settingDao.delete(setting);
        }
    }

}
