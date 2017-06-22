/*
 * cilla - Blog Management System
 *
 * Copyright (C) 2017 Richard "Shred" Körber
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
package org.shredzone.cilla.web.plugin;

import java.util.Objects;

import org.shredzone.cilla.web.format.ReferenceResolver;
import org.shredzone.cilla.web.plugin.annotation.Priority;
import org.shredzone.cilla.ws.TextFormat;

/**
 * A functional interface that is invoked for post-processing formatted text.
 * <p>
 * To implement a {@link PostProcessingTextFormatter}, just register it as a Spring bean.
 * Use {@link Priority} annotation to define the order of the filters to be executed.
 * <p>
 * A {@link CharSequence} is passed to the filter, containing formatted HTML. As result, a
 * {@link CharSequence} with the filtered text is expected. Also, a
 * {@link ReferenceResolver} is passed in for resolving URLs.
 * <p>
 * {@link TextFormat#HTML} and {@link TextFormat#PREFORMATTED} are not post-processed.
 *
 * @author Richard "Shred" Körber
 */
@FunctionalInterface
public interface PostProcessingTextFormatter {

    /**
     * Applies the filter on a {@link CharSequence} and returns a new {@link CharSequence}
     * with the modified text. A {@link ReferenceResolver} is available for resolving
     * internal references.
     *
     * @param t
     *            {@link CharSequence} with the contents to be filtered. If this is a
     *            {@link StringBuilder} instance, its contents <em>may</em> have changed
     *            after invocation, and this instance should not be used any more.
     * @param referenceResolver
     *            A {@link ReferenceResolver} for resolving references to links and images
     * @return {@link CharSequence} with the filtered text.
     */
    CharSequence apply(CharSequence t, ReferenceResolver referenceResolver);

    /**
     * Connects this {@link PostProcessingTextFormatter} with another
     * {@link PostProcessingTextFormatter}. On invocation time, this formatter is applied
     * first, followed by the "after" formatter.
     *
     * @param after
     *            {@link PostProcessingTextFormatter} to be invoked after this formatter
     * @return {@link PostProcessingTextFormatter} instance that will invoke both
     *         formatters
     */
    default PostProcessingTextFormatter andThen(PostProcessingTextFormatter after) {
        Objects.requireNonNull(after);
        return (CharSequence t, ReferenceResolver r) -> after.apply(apply(t, r), r);
    }

}
