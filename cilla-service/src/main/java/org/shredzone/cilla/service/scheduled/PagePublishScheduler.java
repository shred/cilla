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
package org.shredzone.cilla.service.scheduled;

import javax.annotation.Resource;

import org.shredzone.cilla.service.PageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Collects all schedulers for page publications.
 * <p>
 * Schedulers should run in an own component, so the services invoked in the scheduler
 * can create a database transaction.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class PagePublishScheduler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Resource PageService pageService;

    /**
     * Cron job for checking the publication state of pages.
     */
    @Scheduled(cron = "${cron.pagePublishEvent}")
    public void updatePublishedState() {
        log.debug("Scheduled check of page publication state");
        pageService.updatePublishedState();
    }

}
