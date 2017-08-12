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
package org.shredzone.cilla.web.tag;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.service.link.LinkService;
import org.shredzone.cilla.web.format.TextFormatter;
import org.shredzone.cilla.web.util.TagUtils;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.taglib.annotation.Tag;
import org.shredzone.commons.taglib.annotation.TagInfo;
import org.shredzone.commons.taglib.annotation.TagParameter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Formats a text.
 *
 * @author Richard "Shred" Körber
 */
@Tag(type = BodyTag.class)
@TagInfo("Formats a text.")
@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class FormatTag extends BodyTagSupport {
    private static final long serialVersionUID = -1870489798167703697L;

    @Resource
    private transient TextFormatter textFormatter;

    @Resource
    private transient LinkService linkService;

    private Serializable text;
    private Serializable format;
    private Page page;
    private boolean stripHtml;
    private Integer truncate;
    private String var;
    private String scope;

    @TagParameter
    public void setText(Serializable text)      { this.text = text; }

    @TagParameter
    public void setFormat(Serializable format)  { this.format = format; }

    @TagParameter
    public void setPage(Page page)              { this.page = page; }

    @TagParameter
    public void setStripHtml(boolean stripHtml) { this.stripHtml = stripHtml; }

    @TagParameter
    public void setTruncate(Integer truncate)   { this.truncate = truncate; }

    @TagParameter
    public void setVar(String var)              { this.var = var; }

    @TagParameter
    public void setScope(String scope)          { this.scope = scope; }

    @Override
    public int doEndTag() throws JspException {
        CharSequence result;
        TextFormat txtFormat;

        if (text != null && text instanceof FormattedText) {
            if (format != null) {
                throw new IllegalArgumentException("text contains FormattedText, format must not be set");
            }
            result = ((FormattedText) text).getText();
            txtFormat = ((FormattedText) text).getFormat();
        } else {
            if (format != null && format instanceof TextFormat) {
                txtFormat = (TextFormat) format;
            } else if (format != null) {
                txtFormat = TextFormat.valueOf(format.toString());
            } else if (text == null) {
                // if no text and no format was set, render the body as HTML
                txtFormat = TextFormat.HTML;
            } else {
                throw new IllegalArgumentException("format not set");
            }

            if (text != null) {
                result = text.toString();
            } else if (bodyContent != null) {
                result = bodyContent.toString().trim();
            } else {
                result = "";
            }
        }

        if (result == null) result = "";

        result = textFormatter.format(result, txtFormat, () -> linkService.linkTo().page(page));

        if (stripHtml) {
            result = textFormatter.stripHtml(result);
        }

        if (truncate != null && result.length() > truncate) {
            StringBuilder trunc = new StringBuilder(result);
            int truncpos = trunc.lastIndexOf(" ", truncate);
            if (truncpos < truncate - 30) {
                truncpos = truncate;
            }
            trunc.setLength(truncpos);
            trunc.append("\u2026");
            result = trunc;
        }

        if (var != null) {
            TagUtils.setScopedAttribute(pageContext, var, result, scope);

        } else {
            try {
                pageContext.getOut().print(result);
            } catch (IOException ex) {
                throw new JspException(ex);
            }
        }

        return EVAL_PAGE;
    }

}
