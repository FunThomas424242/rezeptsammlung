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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TagView {

    protected List<String> tags;

    public List<String> getTags() {
        return tags;
    }


    protected static Map<String, Long> ratedMap(List<TagView> tags) {
        final Map<String, Long> ratedMap = new HashMap<>();
        tags.stream()
            // TagView -> List<String>
            .flatMap(item -> item.getTags().stream())
            // String  (Tagwert)
            .forEach(tagText -> {
                if (ratedMap.containsKey(tagText)) {
                    ratedMap.put(tagText, (ratedMap.get(tagText) + 1L));
                } else {
                    ratedMap.put(tagText, 1L);
                }
            });
        return ratedMap;
    }

    protected static Set<String> distinct(List<TagView> tags) {
        return tags.stream()
            .flatMap(item -> item.getTags().stream())
            .collect(Collectors.toSet());
    }

    protected static Map<String, String> toSelectionMap(Set<String> tags) {
        return tags.stream().collect(Collectors.toMap(t -> t, t -> t));
    }
}
