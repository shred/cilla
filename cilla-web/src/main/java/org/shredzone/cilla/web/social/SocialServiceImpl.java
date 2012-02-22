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
package org.shredzone.cilla.web.social;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.core.model.Page;
import org.shredzone.cilla.web.plugin.annotation.Priority;
import org.shredzone.cilla.web.social.annotation.SocialBookmark;
import org.shredzone.cilla.web.social.annotation.SocialHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link SocialService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
public class SocialServiceImpl implements SocialService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource ApplicationContext applicationContext;

    private SortedSet<SocialBookmarkInvoker> invokers = new TreeSet<SocialBookmarkInvoker>();

    /**
     * Sets up the service. All beans are scanned for {@link SocialHandler} annotations.
     * If found, {@link SocialBookmark} annotations will further describe the social
     * service.
     */
    @PostConstruct
    public void setup() {
        Collection<Object> beans = applicationContext.getBeansWithAnnotation(SocialHandler.class).values();
        for (Object bean : beans) {
            SocialHandler shAnno = bean.getClass().getAnnotation(SocialHandler.class);
            if (shAnno != null) {
                for (Method method : bean.getClass().getMethods()) {
                    SocialBookmark bookmarkAnno = AnnotationUtils.findAnnotation(method, SocialBookmark.class);
                    if (bookmarkAnno != null) {
                        processBookmarkHandler(bean, method, bookmarkAnno);
                    }
                }
            }
        }
    }

    /**
     * Process a bookmark handler.
     *
     * @param bean
     *            Spring bean of the {@link SocialHandler}
     * @param method
     *            Method annotated with {@link SocialBookmark}
     * @param bookmarkAnno
     *            the {@link SocialBookmark} annotation itself
     */
    private void processBookmarkHandler(Object bean, Method method, SocialBookmark bookmarkAnno) {
        SocialBookmarkInvoker invoker = applicationContext.getBean(SocialBookmarkInvoker.class);
        invoker.setBean(bean);
        invoker.setMethod(method);

        Priority priorityAnno = AnnotationUtils.findAnnotation(method, Priority.class);
        if (priorityAnno != null) {
            invoker.setPriority(priorityAnno.value());
        }

        String name = bookmarkAnno.name();
        if (!StringUtils.hasText(name)) {
            name = method.getName();
            if (name.endsWith("SocialBookmark")) {
                name = name.substring(0, name.length() - "SocialBookmark".length());
            }
        }
        invoker.setName(name);

        if (StringUtils.hasText(bookmarkAnno.icon())) {
            invoker.setIcon(bookmarkAnno.icon());
        }

        invokers.add(invoker);

        log.info("Registered Social Bookmark {} (priority {})", invoker.getName(), invoker.getPriority());
    }

    @Override
    public List<SocialLink> createLinksToPage(Page page) {
        List<SocialLink> links = new ArrayList<SocialLink>();

        for (SocialBookmarkInvoker invoker : invokers) {
            SocialLink link = invoker.invoke(page);
            if (link != null) {
                links.add(link);
            }
        }

        return links;
    }

}
