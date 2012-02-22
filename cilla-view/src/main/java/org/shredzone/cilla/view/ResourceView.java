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
package org.shredzone.cilla.view;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.shredzone.commons.view.annotation.PathPart;
import org.shredzone.commons.view.annotation.View;
import org.shredzone.commons.view.annotation.ViewGroup;
import org.shredzone.commons.view.annotation.ViewHandler;
import org.shredzone.commons.view.exception.ErrorResponseException;
import org.shredzone.commons.view.exception.PageNotFoundException;
import org.shredzone.commons.view.exception.ViewException;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

/**
 * Views for showing a resource.
 *
 * @author Richard "Shred" Körber
 */
@ViewHandler
@Component
public class ResourceView extends AbstractView {

    private final Date lastModified = new Date();
    private final Map<String, String> etagMap = new HashMap<String, String>();
    private final Map<String, Integer> sizeMap = new HashMap<String, Integer>();

    /**
     * Streams a resource.
     */
    @ViewGroup({
        @View(pattern = "/resource/${#package}/${#name}", name="resource"),
    })
    public void resourceView(
            @PathPart("#package") String pack,
            @PathPart("#name") String name,
            HttpServletRequest req, HttpServletResponse resp)
    throws ViewException {
        if (pack.indexOf("..") > -1 || pack.indexOf('/') > -1
                || name.indexOf("..") > -1 || name.indexOf('/') > -1) {
            throw new PageNotFoundException("resource '" + pack + '/' + name + "' not found");
        }

        try {
            String key = setup(pack, name);

            String resourceEtag = etagMap.get(key);
            resp.setHeader("ETag", resourceEtag);

            String headerEtag = req.getHeader("If-None-Match");

            if (headerEtag != null && headerEtag.equals(resourceEtag)) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_MODIFIED);
            }

            if (isNotModifiedSince(req, lastModified)) {
                throw new ErrorResponseException(HttpServletResponse.SC_NOT_MODIFIED);
            }

            resp.setContentLength(sizeMap.get(key));
            resp.setDateHeader("Last-Modified", lastModified.getTime());

            InputStream in = ResourceView.class.getResourceAsStream("/public/" + pack + '/' + name);
            try {
                FileCopyUtils.copy(in, resp.getOutputStream());
            } finally {
                in.close();
            }

        } catch (IOException ex) {
            throw new PageNotFoundException("resource '" + pack + '/' + name + "' not found");
        }
    }

    /**
     * Sets up the internal cache for the resource's etag and content length.
     *
     * @param pack
     *            Resource pack
     * @param name
     *            Resource name
     * @return Map key
     */
    private String setup(String pack, String name) throws IOException {
        String key = pack + '/' + name;

        if (!(etagMap.containsKey(key) && sizeMap.containsKey(key))) {
            InputStream in = null;
            try {
                in = ResourceView.class.getResourceAsStream("/public/" + pack + '/' + name);

                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.reset();

                int counter = 0;

                byte[] buffer = new byte[4096];
                int length;
                while ((length = in.read(buffer)) >= 0) {
                    md5.update(buffer, 0, length);
                    counter += length;
                }

                StringBuilder digest = new StringBuilder();
                digest.append('"');
                for (byte b : md5.digest()) {
                    digest.append(String.format("%02x", b & 0xFF));
                }
                digest.append('"');

                etagMap.put(key, digest.toString());
                sizeMap.put(key, counter);
            } catch (NoSuchAlgorithmException ex) {
                // we expect no exception, since MD5 is a standard digester
                throw new InternalError();
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }

        return key;
    }

}
