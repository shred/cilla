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

import java.lang.annotation.ElementType;

import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.StandardTokenizerFactory;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.cfg.AnalyzerDefMapping;
import org.hibernate.search.cfg.SearchMapping;
import org.shredzone.cilla.core.model.Page;

/**
 * A factory that set ups Lucene for indexing the Cilla entities.
 *
 * @author Richard "Shred" Körber
 */
public class LuceneSearchMappingFactory {

    /**
     * Builds the {@link SearchMapping} to be used.
     *
     * @return the search mapping
     */
    @Factory
    public SearchMapping build() {
        SearchMapping mapping = new SearchMapping();

        setupLanguageAnalyzers(mapping);
        setupIndex(mapping);

        return mapping;
    }

    /**
     * Sets up the index configuration.
     *
     * @param mapping
     *            {@link SearchMapping} to be used
     */
    private void setupIndex(SearchMapping mapping) {
        mapping
            .entity(Page.class)
                .classBridge(PageBridge.class).name("text").index(Index.YES).store(Store.YES)
                .indexed()
                .property("id", ElementType.METHOD).documentId()
                .property("title", ElementType.METHOD).field().index(Index.YES).analyzer(GermanAnalyzer.class)
        ;
    }

    /**
     * Sets up the language analyzers.
     *
     * @param mapping
     *            {@link SearchMapping} to be used
     */
    private void setupLanguageAnalyzers(SearchMapping mapping) {
        AnalyzerDefMapping adm = mapping.analyzerDef("content", StandardTokenizerFactory.class);
        adm.filter(LowerCaseFilterFactory.class);
    }

}
