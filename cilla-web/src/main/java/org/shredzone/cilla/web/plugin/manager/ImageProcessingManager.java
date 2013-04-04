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
package org.shredzone.cilla.web.plugin.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.web.plugin.ImageProcessingFactory;
import org.shredzone.cilla.ws.ImageProcessing;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Manages {@link ImageProcessingFactory}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ImageProcessingManager {

    private @Resource ApplicationContext applicationContext;

    private List<ImageProcessingFactory> factories = new ArrayList<>();

    /**
     * Sets up the manager.
     */
    @PostConstruct
    protected void setup() {
        List<ImageProcessingFactory> collect = new ArrayList<>();
        for (ImageProcessingFactory ipf : applicationContext.getBeansOfType(ImageProcessingFactory.class).values()) {
            collect.add(ipf);
        }
        Collections.sort(collect, new PriorityComparator<>(ImageProcessingFactory.class));
        factories = Collections.unmodifiableList(collect);
    }

    /**
     * Creates an {@link ImageProcessing} of the given type.
     *
     * @param type
     *            {@link ImageProcessing} type
     * @return {@link ImageProcessing}, or {@code null} if there is no such type
     */
    public ImageProcessing createImageProcessing(String type) {
        for (ImageProcessingFactory ipf : factories) {
            ImageProcessing result = ipf.createImageProcessing(type);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

}
