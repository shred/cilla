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
package org.shredzone.cilla.service.resource;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.shredzone.cilla.core.datasource.ResourceDataSource;
import org.shredzone.cilla.ws.ImageProcessing;
import org.shredzone.cilla.ws.exception.CillaServiceException;
import org.springframework.stereotype.Component;

/**
 * Tool for handling images.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class ImageTools {

    private @Resource ImageProcessor imageProcessor;

    /**
     * Processes an image.
     *
     * @param src
     *            Original image
     * @param process
     *            {@link ImageProcessing} with processing data, or {@code null} to keep
     *            the original
     * @return Processed image
     */
    public ResourceDataSource processImage(ResourceDataSource src, ImageProcessing process)
    throws CillaServiceException {
        if (process == null) {
            return src;
        }

        try {
            ImageProcessorResult result = imageProcessor.process(src, src.getId(), process);
            return new ByteArrayResourceDataSource(src, result.getContentType(), result.getData());
        } catch (IOException ex) {
            throw new CillaServiceException(ex);
        }
    }

    /**
     * Creates an {@link ExifAnalyzer} that analyzes the given {@link DataSource}.
     *
     * @param src
     *            {@link DataSource} to analyze
     * @return {@link ExifAnalyzer}
     */
    public ExifAnalyzer createExifAnalyzer(DataSource src) throws CillaServiceException {
        try {
            return ExifAnalyzer.create(src.getInputStream());
        } catch (IOException ex) {
            throw new CillaServiceException(ex);
        }
    };

    /**
     * Analyzes the dimension of the given image {@link DataSource}.
     *
     * @param src
     *            {@link DataSource} to analyze
     * @return image dimensions
     */
    public Dimension analyzeDimension(DataSource src) throws CillaServiceException {
        try {
            BufferedImage image = ImageIO.read(src.getInputStream());
            return (image != null ? new Dimension(image.getWidth(), image.getHeight()) : null);
        } catch (IOException ex) {
            throw new CillaServiceException(ex);
        }
    }

}
