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
package org.shredzone.cilla.web.fragment.manager;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.web.fragment.annotation.Fragment;
import org.shredzone.cilla.web.fragment.annotation.FragmentRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Manages all {@link FragmentRenderer}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class FragmentManager {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource ApplicationContext applicationContext;

    private Map<String, FragmentInvoker> fragmentMap = new HashMap<String, FragmentInvoker>();

    /**
     * Gets a {@link FragmentInvoker} for the given fragment name.
     *
     * @param name
     *            Fragment name
     * @return {@link FragmentInvoker} for that name, or {@code null} if there is no such
     *         fragment
     */
    public FragmentInvoker getInvoker(String name) {
        return fragmentMap.get(name);
    }

    /**
     * Sets up the {@link FragmentManager} after construction.
     */
    @PostConstruct
    protected void setup() {
        Collection<Object> beans = applicationContext.getBeansWithAnnotation(FragmentRenderer.class).values();
        for (Object bean : beans) {
            FragmentRenderer fhAnno = bean.getClass().getAnnotation(FragmentRenderer.class);
            if (fhAnno != null) {
                for (Method method : bean.getClass().getMethods()) {
                    Fragment fragmentAnno = AnnotationUtils.findAnnotation(method, Fragment.class);
                    if (fragmentAnno != null) {
                        processFragment(bean, method, fragmentAnno);
                    }
                }
            }
        }
    }

    /**
     * Processes a {@link Fragment} method.
     *
     * @param bean
     *            Target bean
     * @param method
     *            Target method
     * @param anno
     *            {@link Fragment} annotation
     */
    private void processFragment(Object bean, Method method, Fragment anno) {
        String name = computeFragmentName(method, anno);

        if (fragmentMap.containsKey(name)) {
            throw new IllegalStateException("Fragment '" + name + "' defined twice");
        }

        String template = null;
        if (anno.template() != null && !anno.template().isEmpty()) {
            template = anno.template();
        }

        FragmentInvoker invoker = new FragmentInvoker(bean, method, template);
        fragmentMap.put(name, invoker);

        log.info("Found fragment '{}'", name);
    }

    /**
     * Computes a fragment name. If the {@link Fragment} annotation contains a name, it is
     * used. If no name is given, it is guessed by the method name. If the method name
     * ends with "Fragment", it is removed.
     *
     * @param method
     *            {@link Method} of the view handler
     * @param anno
     *            {@link Fragment} annotation
     * @return fragment name to be used for this view
     */
    private String computeFragmentName(Method method, Fragment anno) {
        if (StringUtils.hasText(anno.name())) {
            return anno.name();
        }

        String name = method.getName();
        if (name.length() > 8 && name.endsWith("Fragment")) {
            name = name.substring(0, name.length() - "Fragment".length());
        }

        return name;
    }

}
