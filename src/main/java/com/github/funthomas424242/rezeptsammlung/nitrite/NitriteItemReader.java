package com.github.funthomas424242.rezeptsammlung.nitrite;

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

import com.github.funthomas424242.sbstarter.nitrite.NitriteRepository;
import org.dizitart.no2.FindOptions;
import org.dizitart.no2.SortOrder;
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;

import static org.dizitart.no2.FindOptions.sort;
import static org.dizitart.no2.objects.filters.ObjectFilters.and;
import static org.dizitart.no2.objects.filters.ObjectFilters.eq;
import static org.dizitart.no2.objects.filters.ObjectFilters.gt;

public class NitriteItemReader<T> implements ItemReader<T> {

    protected final Logger LOG = LoggerFactory.getLogger(NitriteItemReader.class);

    protected final NitriteRepository<T> repo;
    protected final ObjectFilter filter;
    protected final FindOptions option;

    protected Iterator<T> resultIterator;

    public NitriteItemReader(NitriteRepository<T> repo) {
        this.repo = repo;
        this.filter = and(gt("age", 30), eq("blha", "blup"));
        this.option = sort("age", SortOrder.Ascending);
        this.resultIterator = null;
        LOG.debug("### Reader initialized for repo type: " + repo.getName());
    }


    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (resultIterator == null) {
            final Cursor<T> resultCusor = repo.find();
            LOG.debug("### ResultCursor initialiced with size "
                + resultCusor.size()
                + " from "
                + resultCusor.totalCount() + " for repo type: " + repo.getName());
            resultIterator = resultCusor.iterator();
        }
        if (resultIterator.hasNext()) {
            final T element = resultIterator.next();
            LOG.debug("### GELESEN: " + element);
            return element;
        } else {
            LOG.debug("### Lesen beendet für repo typ: " + repo.getName());
            repo.close();
            return null;
        }
    }

}
