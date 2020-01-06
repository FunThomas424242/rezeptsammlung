package com.github.funthomas424242.rezeptsammlung.updater;

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

import com.github.funthomas424242.rezeptsammlung.crawler.SiteUrl;
import com.github.funthomas424242.rezeptsammlung.rezept.Rezept;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SiteUrl2RezeptItemProcessor implements ItemProcessor<SiteUrl, List<Rezept>> {

    protected static final Logger LOG = LoggerFactory.getLogger(SiteUrl2RezeptItemProcessor.class);

    @Override
    public List<Rezept> process(final SiteUrl rezeptSite) throws Exception {
        try {
            final Rezept[] rezeptNew = Rezept.of(rezeptSite.getUrl());
            LOG.info("Converting ({}) into ({})", rezeptSite, rezeptNew);
            return Arrays.asList(rezeptNew);
        } catch (IllegalArgumentException | IOException ex) {
            LOG.error("Converting failed with ({})", rezeptSite);
        }
        return null;
    }

}
