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
package org.shredzone.cilla.web.plugin.manager;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.web.plugin.LinkTypeMatcher;
import org.shredzone.cilla.web.plugin.LocalLinkResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Manages all {@link LinkTypeMatcher} and {@link LocalLinkResolver} beans.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class LinkResolverManager {

    private @Resource ApplicationContext applicationContext;

    private List<LinkTypeMatcher> linkTypeMatchers;
    private List<LocalLinkResolver> localLinkResolvers;

    @PostConstruct
    protected void setup() {
        linkTypeMatchers = Collections.unmodifiableList(
                applicationContext.getBeansOfType(LinkTypeMatcher.class).values().stream()
                        .sorted(new PriorityComparator<>(LinkTypeMatcher.class))
                        .collect(toList()));

        localLinkResolvers = Collections.unmodifiableList(
                applicationContext.getBeansOfType(LocalLinkResolver.class).values().stream()
                        .sorted(new PriorityComparator<>(LocalLinkResolver.class))
                        .collect(toList()));
    }

    /**
     * Gets all defined {@link LinkTypeMatcher} ordered by priority.
     *
     * @return {@link LinkTypeMatcher} collection, may be empty but never {@code null}
     */
    public Collection<LinkTypeMatcher> getLinkTypeMatchers() {
        return linkTypeMatchers;
    }

    /**
     * Gets all defined {@link LocalLinkResolver} ordered by priority.
     *
     * @return {@link LocalLinkResolver} collection, may be empty but never {@code null}
     */
    public Collection<LocalLinkResolver> getLocalLinkResolvers() {
        return localLinkResolvers;
    }

}
