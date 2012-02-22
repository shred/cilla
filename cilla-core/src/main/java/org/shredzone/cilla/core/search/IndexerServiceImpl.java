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
package org.shredzone.cilla.core.search;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link IndexerService}.
 *
 * @author Richard "Shred" Körber
 */
@Component
public class IndexerServiceImpl implements IndexerService, Runnable {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private @Value("${search.reindexOnStartup}") boolean reindexOnStartup;

    private @Resource SessionFactory sessionFactory;

    private boolean running = false;

    /**
     * Setting up the indexer. Starts a reindex if configured.
     */
    @PostConstruct
    public void setup() {
        if (reindexOnStartup) {
            reindex();
        }
    }

    @Override
    public void reindex() {
        synchronized (this) {
            if (running) {
                return;
            }
            running = true;
        }

        Thread th = new Thread(this);
        th.setName("Indexer Thread");
        th.start();
    }

    @Override
    public void run() {
        Session session = null;
        try {
            log.info("Starting full-database indexing");
            long start = System.currentTimeMillis();

            session = sessionFactory.openSession();

            FullTextSession fullTextSession = Search.getFullTextSession(session);
            fullTextSession.createIndexer().startAndWait();

            long end = System.currentTimeMillis();
            log.info("Full-database indexing completed in " + (end - start) + " ms");

        } catch (Exception ex) {
            log.error("Full-database indexing failed", ex);

        } finally {
            if (session != null) {
                session.close();
                session = null;
            }

            synchronized (this) {
                running = false;
            }
        }
    }

}
