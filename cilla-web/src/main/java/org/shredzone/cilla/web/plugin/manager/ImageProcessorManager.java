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
package org.shredzone.cilla.web.plugin.manager;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.web.plugin.ImageProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Manages all available image processors.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ImageProcessorManager {

    private @Resource ApplicationContext applicationContext;

    private List<ImageProcessor> processors;

    /**
     * Sets up the manager.
     */
    @PostConstruct
    protected void setup() {
        processors = Collections.unmodifiableList(
                applicationContext.getBeansOfType(ImageProcessor.class).values().stream()
                    .sorted(new PriorityComparator<>(ImageProcessor.class))
                    .collect(Collectors.toList()));
    }

    /**
     * Gets a list of all {@link ImageProcessor} instances, in the order of their
     * priority.
     */
    public List<ImageProcessor> getImageProcessors() {
        return processors;
    }

}
