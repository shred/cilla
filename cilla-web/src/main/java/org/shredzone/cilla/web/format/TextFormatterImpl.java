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
package org.shredzone.cilla.web.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.web.plugin.PostProcessingTextFilter;
import org.shredzone.cilla.web.plugin.manager.PriorityComparator;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.text.TextFilter;
import org.shredzone.commons.text.filter.HtmlEscapeFilter;
import org.shredzone.commons.text.filter.ParagraphFilter;
import org.shredzone.commons.text.filter.SimplifyHtmlFilter;
import org.shredzone.commons.text.filter.StripHtmlFilter;
import org.shredzone.commons.text.filter.TextileFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Formats the given {@link FormattedText} into HTML.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class TextFormatterImpl implements TextFormatter {
    private static final ParagraphFilter PARAGRAPH_FILTER = new ParagraphFilter();
    private static final HtmlEscapeFilter HTML_ESCAPE_FILTER = new HtmlEscapeFilter();
    private static final SimplifyHtmlFilter HTML_SIMPLIFY_FILTER = new SimplifyHtmlFilter();
    private static final StripHtmlFilter STRIP_HTML_FILTER = new StripHtmlFilter();

    private @Resource ApplicationContext applicationContext;

    private List<TextFilter> postFilters;

    /**
     * Initializes the text formatter.
     */
    @PostConstruct
    protected void setup() {
        List<TextFilter> filterList = new ArrayList<TextFilter>();
        for (PostProcessingTextFilter filter : applicationContext.getBeansOfType(PostProcessingTextFilter.class).values()) {
            filterList.add(filter);
        }
        Collections.sort(filterList, new PriorityComparator<TextFilter>(TextFilter.class));
        postFilters = Collections.unmodifiableList(filterList);
    }

    @Override
    public CharSequence format(FormattedText text) {
        return format(text, null);
    }

    @Override
    public CharSequence format(FormattedText text, Page page) {
        return format(text.getText(), text.getFormat(), page);
    }

    @Override
    public CharSequence format(CharSequence text, TextFormat format, Page page) {
        StringBuilder sb = new StringBuilder(text);

        switch (format) {
            case HTML:
                break;

            case PLAIN:
                sb = HTML_ESCAPE_FILTER.filter(sb);
                sb = PARAGRAPH_FILTER.filter(sb);
                sb = applyPostProcessingTextFilters(sb);
                break;

            case SIMPLIFIED:
                sb = HTML_SIMPLIFY_FILTER.filter(sb);
                sb = PARAGRAPH_FILTER.filter(sb);
                sb = applyPostProcessingTextFilters(sb);
                break;

            case PARAGRAPHED:
                sb = PARAGRAPH_FILTER.filter(sb);
                sb = applyPostProcessingTextFilters(sb);
                break;

            case PREFORMATTED:
                sb = HTML_ESCAPE_FILTER.filter(sb);
                sb.insert(0, "<pre>");
                sb.append("</pre>");
                break;

            case TEXTILE:
                ReferenceResolver rr = applicationContext.getBean(ReferenceResolver.class);
                rr.setPage(page);

                TextileFilter tf = new TextileFilter();
                tf.setAnalyzer(rr);

                sb = tf.filter(sb);
                sb = applyPostProcessingTextFilters(sb);
                break;

            default:
                throw new IllegalArgumentException("Cannot handle format " + format);
        }

        return sb;
    }

    @Override
    public CharSequence stripHtml(CharSequence str) {
        return STRIP_HTML_FILTER.filter(new StringBuilder(str));
    }

    /**
     * Applies all {@link PostProcessingTextFilter} to the {@link StringBuilder}, in
     * prioritized order.
     *
     * @param sb
     *            {@link StringBuilder} to change
     * @return new {@link StringBuilder} with the filters applied
     */
    private StringBuilder applyPostProcessingTextFilters(StringBuilder sb) {
        StringBuilder result = sb;
        for (TextFilter filter : postFilters) {
            result = filter.filter(result);
        }
        return result;
    }

}
