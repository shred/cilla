/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2016 Richard "Shred" Körber
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
package org.shredzone.cilla.core.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.activation.DataSource;

import org.springframework.stereotype.Component;

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;

/**
 * A class for analyzing files and returning a matching mime-type.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class MimeTypeAnalyzer {

    private final ContentInfoUtil util = new ContentInfoUtil();

    /**
     * Analyzes an {@link InputStream} and returns a detected mime-type.
     *
     * @param in
     *            {@link InputStream} to read. Must be fresh and will be closed after
     *            reading.
     * @return Optional mime-type
     */
    public Optional<String> analyze(InputStream in) throws IOException {
        try {
            ContentInfo ci = util.findMatch(in);
            return Optional.ofNullable(ci.getMimeType());
        } finally {
            in.close();
        }
    }

    /**
     * Analyzes a {@link DataSource}. If it was able to find a mime type for the content,
     * it is returned. If the content could not be analyzed, the
     * {@link DataSource#getContentType()} is returned instead.
     *
     * @param ds
     *            {@link DataSource} to analyze
     * @return Mime type of the source content
     */
    public String analyze(DataSource ds) throws IOException {
        Optional<String> mime = analyze(ds.getInputStream());
        return mime.orElseGet(() -> ds.getContentType());
    }

}
