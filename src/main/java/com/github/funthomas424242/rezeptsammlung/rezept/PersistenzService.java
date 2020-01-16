package com.github.funthomas424242.rezeptsammlung.rezept;

/*-
 * #%L
 * rezeptsammlung
 * %%
 * Copyright (C) 2019 - 2020 PIUG
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.github.funthomas424242.sbstarter.nitrite.NitriteRepository;
import com.github.funthomas424242.sbstarter.nitrite.NitriteTemplate;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.dizitart.no2.IndexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.dizitart.no2.IndexOptions.indexOptions;

@Service
public class PersistenzService {

    @Autowired
    protected NitriteTemplate nitriteTemplate;

    protected NitriteRepository<Rezept> getRezeptRepository() {
        final NitriteRepository<Rezept> repository = nitriteTemplate.getRepository(Rezept.class);
        // Da Indizes permanent in der Datenbank gespeichert werden,
        // dürfen diese nur 1x angelegt werden.
        // können aber async erstellt werden, da nur mit geringen Datenmengen gerechnet wird.
        if (!repository.hasIndex("id")) {
            repository.createIndex("id", indexOptions(IndexType.Unique, true));
        }
        if (!repository.hasIndex("titel")) {
            repository.createIndex("titel", indexOptions(IndexType.Fulltext, true));
        }
        if (!repository.hasIndex("tag")) {
            repository.createIndex("tag", indexOptions(IndexType.NonUnique, true));
        }
        return repository;
    }

    /**
     * https://stackoverflow.com/questions/24968697/how-to-implement-auto-suggest-using-lucenes-new-analyzinginfixsuggester-api
     *
     * @param suchTags liste der Suchwörter (vom Nutzer eingegeben)
     * @return Set der passenden Tags
     */
    public Set<String> matchingTags(final String suchTags) {
        final NitriteRepository<Rezept> repository = getRezeptRepository();
        final List<TagView> tags = repository.find().project(TagView.class).toList();
        final Map<String, Long> ratedTagMap = TagView.ratedMap(tags);
        repository.close();
        try {
            RAMDirectory index_dir = new RAMDirectory();
            StandardAnalyzer analyzer = new StandardAnalyzer();
            AnalyzingInfixSuggester suggester = new AnalyzingInfixSuggester(
                index_dir, analyzer);
            suggester.build(new TagsInputIterator(ratedTagMap));
            final Set<BytesRef> tmpSet = new HashSet<BytesRef>();
            final List<Lookup.LookupResult> results = suggester.lookup(suchTags, tmpSet, true, 10);

            return results.stream()
                .map(result -> result.key.toString())
                .collect(Collectors.toSet());

        } catch (IOException ex) {
            System.out.println("Das hätte nicht passieren sollen ;) " + ex);
        }
        return new HashSet<>();
    }

    public Set<String> allTags() {
        final NitriteRepository<Rezept> repository = getRezeptRepository();
        final List<TagView> tags = repository.find().project(TagView.class).toList();
        repository.close();
        return TagView.distinct(tags);
    }

    public List<Rezept> allRezepte() {
        final NitriteRepository<Rezept> repository = getRezeptRepository();
        final List<Rezept> rezepte = repository.find().toList();
        repository.close();
        return rezepte;
    }

    public List<Rezept> clearRezeptsammlung() {
        getRezeptRepository().drop();
        return allRezepte();
    }
}
