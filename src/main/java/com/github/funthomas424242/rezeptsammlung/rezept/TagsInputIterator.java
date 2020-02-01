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

import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TagsInputIterator implements InputIterator {

    protected final Map<String, Long> ratedTagMap;
    protected final Iterator<String> tags;
    protected String currentTag;

    public TagsInputIterator(Map<String, Long> ratedTagMap) {
        this.ratedTagMap = ratedTagMap;
        this.tags = ratedTagMap.keySet().iterator();
    }

    @Override
    public long weight() {
        return ratedTagMap.get(currentTag);
    }

    // Die Wörter aus dem Suchstring, welche infix matchen
    @Override
    public Set<BytesRef> contexts() {
        return null;
    }

    @Override
    public boolean hasContexts() {
        return false;
    }

    /**
     * This method needs to return the key for the record; this is the
     * text we'll be autocompleting against.
     *
     * @return ByteRef of tag
     */
    @Override
    public BytesRef next() {
        if (tags.hasNext()) {
            currentTag = tags.next();
            try {
                return new BytesRef(currentTag.getBytes("UTF8"));
            } catch (UnsupportedEncodingException e) {
                throw new Error("Couldn't convert to UTF-8");
            }
        } else {
            return null;
        }
    }

    @Override
    public BytesRef payload() {
        return null;
    }

    @Override
    public boolean hasPayloads() {
        return false;
    }
}
