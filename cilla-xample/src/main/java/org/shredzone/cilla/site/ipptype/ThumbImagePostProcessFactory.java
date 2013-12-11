/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2013 Richard "Shred" Körber
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
package org.shredzone.cilla.site.ipptype;

import org.shredzone.cilla.web.plugin.ImageProcessingFactory;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.ImageType;
import org.springframework.stereotype.Component;

/**
 * An {@link ImageProcessingFactory} that creates thumbnail and preview images.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ThumbImagePostProcessFactory implements ImageProcessingFactory {

    @Override
    public ImageProcessing createImageProcessing(String type) {
        switch (type) {
            case "thumb":
                return new ImageProcessing(100, 100, ImageType.PNG);

            case "preview":
                return new ImageProcessing(250, 250, ImageType.PNG);

            default:
                return null;
        }
    }

}
