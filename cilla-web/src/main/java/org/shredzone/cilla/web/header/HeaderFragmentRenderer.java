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
package org.shredzone.cilla.web.header;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspWriter;

import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.shredzone.cilla.web.header.tag.HeadTag;
import org.shredzone.cilla.web.header.tag.MetaTag;
import org.springframework.stereotype.Component;

/**
 * Renders a fragment of all additional document header tags.
 *
 * @author Richard "Shred" Körber
 */
@Component
@FragmentRenderer
public class HeaderFragmentRenderer {

    private @Resource DocumentHeaderManager documentHeaderManager;

    /**
     * Renders {@link MetaTag} only.
     */
    @Fragment(name = "meta")
    public void metaFragment(ServletRequest req, JspWriter out) throws IOException {
        DocumentHeader header = documentHeaderManager.getDocumentHeader();
        for (MetaTag meta : header.getMetaTags()) {
            out.println(meta.toString());
        }
    }

    /**
     * Renders {@link HeadTag}, except of {@link MetaTag}.
     */
    @Fragment(name = "header")
    public void headerFragment(ServletRequest req, JspWriter out) throws IOException {
        DocumentHeader header = documentHeaderManager.getDocumentHeader();
        for (HeadTag head : header.getHeadTags()) {
            out.println(head.toString());
        }
    }

}
