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

import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.web.plugin.LinkTypeMatcher;
import org.shredzone.cilla.web.plugin.LocalLinkResolver;
import org.shredzone.cilla.web.plugin.manager.LinkResolverManager;
import org.shredzone.commons.text.LinkAnalyzer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Resolves references to URLs and link types.
 *
 * @author Richard "Shred" Körber
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ReferenceResolver implements LinkAnalyzer {
    private static final Pattern ABSOLUTE_URL = Pattern.compile("([a-zA-Z]+\\:\\/\\/.*)|(\\/.*)");

    private @Resource LinkResolverManager linkResolverManager;

    private Page page;

    public Page getPage()                       { return page; }
    public void setPage(Page page)              { this.page = page; }

    @Override
    public String linkType(String url) {
        for (LinkTypeMatcher m : linkResolverManager.getLinkTypeMatchers()) {
            String result = m.resolveLinkType(url);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    @Override
    public String linkUrl(String url) {
        if (page != null && !ABSOLUTE_URL.matcher(url).matches()) {
            for (LocalLinkResolver r : linkResolverManager.getLocalLinkResolvers()) {
                String result = r.resolveLocalLink(url, page, false);
                if (result != null) {
                    return result;
                }
            }
        }
        return url;
    }

    @Override
    public String imageUrl(String url) {
        if (page != null && !ABSOLUTE_URL.matcher(url).matches()) {
            for (LocalLinkResolver r : linkResolverManager.getLocalLinkResolvers()) {
                String result = r.resolveLocalLink(url, page, true);
                if (result != null) {
                    return result;
                }
            }
        }
        return url;
    }

}
