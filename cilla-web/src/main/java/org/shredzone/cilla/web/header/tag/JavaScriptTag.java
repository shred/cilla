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
package org.shredzone.cilla.web.header.tag;

/**
 * A JavaScript tag.
 *
 * @author Richard "Shred" Körber
 */
public class JavaScriptTag implements HeadTag {

    private final StringBuilder body = new StringBuilder();

    /**
     * Appends a JavaScript code block to the JavaScript section. The code block must
     * not be wrapped in &lt;script&gt; tags!
     *
     * @param script
     *            code block to add
     * @return itself
     */
    public JavaScriptTag append(String script) {
        if (script.contains("]]>")) {
            throw new IllegalArgumentException("Script contains ']]>'");
        }
        body.append(script);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<script type=\"text/javascript\">//<![CDATA[\n")
            .append(body)
            .append(" //]]></script>");

        return sb.toString();
    }

}
