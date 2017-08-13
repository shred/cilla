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
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.core.model.embed.FormattedText;
import org.shredzone.cilla.service.link.LinkBuilder;
import org.shredzone.cilla.web.plugin.PostProcessingTextFormatter;
import org.shredzone.cilla.web.plugin.manager.PriorityComparator;
import org.shredzone.cilla.ws.TextFormat;
import org.shredzone.commons.text.filter.HtmlEscapeFilter;
import org.shredzone.commons.text.filter.KeepFilter;
import org.shredzone.commons.text.filter.MarkdownFilter;
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

    private PostProcessingTextFormatter postProcessingTextFormatters = (t, r) -> t;

    /**
     * Initializes the text formatter.
     */
    @PostConstruct
    protected void setup() {
        applicationContext.getBeansOfType(PostProcessingTextFormatter.class).values().stream()
                .sorted(new PriorityComparator<>(PostProcessingTextFormatter.class))
                .forEachOrdered(filter -> {
                    postProcessingTextFormatters = postProcessingTextFormatters.andThen(filter);
                });
    }

    @Override
    public CharSequence format(FormattedText text) {
        return format(text, null);
    }

    @Override
    public CharSequence format(FormattedText text, Supplier<LinkBuilder> linkBuilderSupplier) {
        return format(text.getText(), text.getFormat(), linkBuilderSupplier);
    }

    @Override
    public CharSequence format(CharSequence text, TextFormat format) {
        return format(text, format, null);
    }

    @Override
    public CharSequence format(CharSequence text, TextFormat format, Supplier<LinkBuilder> linkBuilderSupplier) {
         return formatting(format, linkBuilderSupplier).apply(text);
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
     * @param linkBuilderSupplier
     *            supplier for a preconfigured {@link LinkBuilder} to be used, or
     *            {@code null} to generate relative links
     * @return {@link Function} formatting the {@link TextFormat}. Can be a singleton or a
     *         new instance.
     */
    private Function<CharSequence, CharSequence> formatting(TextFormat format, Supplier<LinkBuilder> linkBuilderSupplier) {
        switch (Objects.requireNonNull(format)) {
            case HTML:
                return KEEP_FILTER;

            case PLAIN:
                return HTML_ESCAPE_FILTER
                        .andThen(PARAGRAPH_FILTER)
                        .andThen(curryFormatters(null));

            case SIMPLIFIED:
                return HTML_SIMPLIFY_FILTER
                        .andThen(PARAGRAPH_FILTER)
                        .andThen(curryFormatters(null));

            case PARAGRAPHED:
                return PARAGRAPH_FILTER
                        .andThen(curryFormatters(null));

            case PREFORMATTED:
                return HTML_ESCAPE_FILTER
                        .andThen(ch -> "<pre>" + ch + "</pre>");

            case TEXTILE:
                ReferenceResolver rr = applicationContext.getBean(ReferenceResolver.class);
                rr.setLinkBuilderSupplier(linkBuilderSupplier);

                TextileFilter tf = new TextileFilter();
                tf.setAnalyzer(rr);

                return tf.andThen(curryFormatters(rr));

            case MARKDOWN:
                ReferenceResolver rrmd = applicationContext.getBean(ReferenceResolver.class);
                rrmd.setLinkBuilderSupplier(linkBuilderSupplier);

                MarkdownFilter mf = new MarkdownFilter();
                mf.setAnalyzer(rrmd);

                return mf.andThen(curryFormatters(rrmd));

            default:
                throw new IllegalArgumentException("Cannot handle format " + format);
        }
    }

    /**
     * Curries the postProcessingTextFormatters to use the {@link LinkBuilder}.
     *
     * @param referenceResolver
     *            {@link ReferenceResolver} to be used. May be {@code null} if the
     *            text format used does not allow reference resolving.
     * @return A filter that applies all postProcessingTextFormatters using the given
     *         linkBuilderSupplier
     */
    private Function<CharSequence, CharSequence> curryFormatters(ReferenceResolver referenceResolver) {
        return t -> postProcessingTextFormatters.apply(t, referenceResolver);
    }

}
