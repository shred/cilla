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
package org.shredzone.cilla.core.event;

import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.shredzone.cilla.core.event.annotation.EventListener;
import org.shredzone.cilla.core.event.annotation.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link EventService}.
 *
 * @author Richard "Shred" Körber
 */
@Service
public class EventServiceImpl implements EventService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource ApplicationContext applicationContext;

    private final Map<EventType, List<EventInvoker>> invokerMap = new EnumMap<>(EventType.class);

    /**
     * Sets up the {@link EventService}. It scans for all beans annotated with
     * {@link EventListener}, and registers the events and handler methods.
     */
    @PostConstruct
    protected void setup() {
        applicationContext.getBeansWithAnnotation(EventListener.class).values().forEach(bean -> {
            for (Method method : bean.getClass().getMethods()) {
                OnEvent evAnno = AnnotationUtils.findAnnotation(method, OnEvent.class);
                if (evAnno != null) {
                    processEventMethod(evAnno, bean, method);
                }
            }
        });
    }

    /**
     * Processes a single event handler method that was found.
     *
     * @param anno
     *            {@link OnEvent} annotation with further details about the handler
     * @param bean
     *            bean containing the handler
     * @param method
     *            reference to the handler method
     */
    private void processEventMethod(OnEvent anno, Object bean, Method method) {
        EventInvoker invoker = applicationContext.getBean(EventInvoker.class);
        invoker.setBean(bean);
        invoker.setMethod(method);

        EventType[] events = anno.value();
        if (events == null || events.length == 0) {
            // Invoke on all events if there was no explicit event type given
            events = EventType.values();
        }

        for (EventType type : events) {
            invokerMap.computeIfAbsent(type, it -> new LinkedList<>()).add(invoker);
        }
    }

    @Override
    public void fireEvent(Event<?> event) {
        if (log.isDebugEnabled()) {
            log.debug("Sending event " + event.getType() + ", source = " + event.getSource());
        }

        List<EventInvoker> listeners = invokerMap.get(event.getType());
        if (listeners != null) {
            listeners.forEach(invoker -> invoker.invoke(event));
        }
    }

}
