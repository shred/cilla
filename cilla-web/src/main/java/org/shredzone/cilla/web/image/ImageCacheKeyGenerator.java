/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2017 Richard "Shred" Körber
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
package org.shredzone.cilla.web.image;

import java.lang.reflect.Method;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

/**
 * Generates a cache key for a cached image.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ImageCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        ResourceDataSource image = (ResourceDataSource) params[0];
        ImageOrigin source = (ImageOrigin) params[1];
        String type = (String) params[2];

        StringBuilder sb = new StringBuilder();
        sb.append(image.getId());
        if (image.getEtag() != null) {
            sb.append('#').append(image.getEtag());
        } else {
            sb.append('.').append(image.getLastModified().getTime());
        }
        sb.append(',').append(source.name());
        if (type != null) {
            sb.append(',').append(type);
        }

        return sb.toString();
    }

}
