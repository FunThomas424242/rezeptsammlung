package com.github.funthomas424242.rezeptsammlung.crawler;

/*-
 * #%L
 * rezeptsammlung
 * %%
 * Copyright (C) 2019 PIUG
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

import com.github.funthomas424242.rezeptsammlung.rezept.Rezept;
import com.github.funthomas424242.rezeptsammlung.rezept.RezeptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;
import java.util.List;

public class RezeptItemProcessor implements ItemProcessor<Rezept, Rezept> {

    private static final Logger log = LoggerFactory.getLogger(RezeptItemProcessor.class);

    @Override
    public Rezept process(final Rezept uri) throws Exception {

        // Lies JSON from uri
        // erzeuge Rezept from JSON
        final String titel = "Das beste im Kuchen";
        final List<String> tags = Arrays.asList("vegan", "mit Fleisch");

        final Rezept rezeptNew = new RezeptBuilder().withTitel(titel).withTags(tags).build();

        log.info("Converting (" + uri + ") into (" + rezeptNew + ")");

        return rezeptNew;
    }

}
