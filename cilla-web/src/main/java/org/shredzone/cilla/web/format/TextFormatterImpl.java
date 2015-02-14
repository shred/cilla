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

import java.util.Objects;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.web.plugin.PostProcessingTextFilter;
import org.shredzone.cilla.web.plugin.manager.PriorityComparator;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.text.TextFilter;
import org.shredzone.commons.text.filter.HtmlEscapeFilter;
import org.shredzone.commons.text.filter.KeepFilter;
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
    private static final KeepFilter KEEP_FILTER = new KeepFilter();
    private static final ParagraphFilter PARAGRAPH_FILTER = new ParagraphFilter();
    private static final HtmlEscapeFilter HTML_ESCAPE_FILTER = new HtmlEscapeFilter();
    private static final SimplifyHtmlFilter HTML_SIMPLIFY_FILTER = new SimplifyHtmlFilter();
    private static final StripHtmlFilter STRIP_HTML_FILTER = new StripHtmlFilter();

    private @Resource ApplicationContext applicationContext;

    private Function<CharSequence, CharSequence> postProcessingTextFilters = KEEP_FILTER;

    /**
     * Initializes the text formatter.
     */
    @PostConstruct
    protected void setup() {
        applicationContext.getBeansOfType(PostProcessingTextFilter.class).values().stream()
                .sorted(new PriorityComparator<>(TextFilter.class))
                .forEachOrdered(filter -> {
                    postProcessingTextFilters = postProcessingTextFilters.andThen(filter);
                });
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
        return formatting(format, page).apply(text);
    }

    @Override
    public CharSequence stripHtml(CharSequence str) {
        return STRIP_HTML_FILTER.apply(str);
    }

    /**
     * Returns a formatting {@link Function} for the given {@link TextFormat}.
     *
     * @param format
     *            {@link TextFormat} to return a formatting function of
     * @param page
     *            {@link Page} context, may be {@code null}
     * @return {@link Function} formatting the {@link TextFormat}. Can be a singleton or a
     *         new instance.
     */
    private Function<CharSequence, CharSequence> formatting(TextFormat format, Page page) {
        switch (Objects.requireNonNull(format)) {
            case HTML:
                return KEEP_FILTER;

            case PLAIN:
                return HTML_ESCAPE_FILTER
                        .andThen(PARAGRAPH_FILTER)
                        .andThen(postProcessingTextFilters);

            case SIMPLIFIED:
                return HTML_SIMPLIFY_FILTER
                        .andThen(PARAGRAPH_FILTER)
                        .andThen(postProcessingTextFilters);

            case PARAGRAPHED:
                return PARAGRAPH_FILTER
                        .andThen(postProcessingTextFilters);

            case PREFORMATTED:
                return HTML_ESCAPE_FILTER
                        .andThen(ch -> "<pre>" + ch + "</pre>");

            case TEXTILE:
                ReferenceResolver rr = applicationContext.getBean(ReferenceResolver.class);
                rr.setPage(page);

                TextileFilter tf = new TextileFilter();
                tf.setAnalyzer(rr);

                return tf.andThen(postProcessingTextFilters);

            default:
                throw new IllegalArgumentException("Cannot handle format " + format);
        }
    }

}
