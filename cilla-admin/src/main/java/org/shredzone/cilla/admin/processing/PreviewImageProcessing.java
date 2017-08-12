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
package org.shredzone.cilla.admin.processing;

import javax.annotation.PostConstruct;

import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.ImageType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Preconfigured {@link ImageProcessing} for previews.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Qualifier("preview")
public class PreviewImageProcessing extends ImageProcessing {
    private static final long serialVersionUID = 2010301450928852803L;

    @Value("${previewWidth}")
    private transient int previewWidth;

    @Value("${previewHeight}")
    private transient int previewHeight;

    @PostConstruct
    protected void setup() {
        setDimension(previewWidth, previewHeight);
        setType(ImageType.PNG);
    }

}
