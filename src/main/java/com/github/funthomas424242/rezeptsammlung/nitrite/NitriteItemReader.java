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
import org.dizitart.no2.objects.Cursor;
import org.dizitart.no2.objects.ObjectFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.Iterator;

public class NitriteItemReader<T> implements ItemReader<T> {

    protected final Logger LOG = LoggerFactory.getLogger(NitriteItemReader.class);

    protected final NitriteRepository<T> repo;
    protected final ObjectFilter filter;
    protected final FindOptions options;

    protected Iterator<T> resultIterator;

    public NitriteItemReader(final NitriteRepository<T> repo) {
        this(repo, null, null);
    }

    public NitriteItemReader(final NitriteRepository<T> repo, final ObjectFilter filter) {
        this(repo, filter, null);
    }

    public NitriteItemReader(final NitriteRepository<T> repo, final FindOptions options) {
        this(repo, null, options);
    }

    public NitriteItemReader(final NitriteRepository<T> repo, final ObjectFilter filter, final FindOptions options) {
        this.repo = repo;
        this.filter = filter;
        this.options = options;
        this.resultIterator = null;
        LOG.debug("### Reader initialized for repo type: " + repo.getName());
    }


    protected Cursor<T> getQuery(){
        if(this.filter == null && this.options == null){
            return repo.find();
        }else if( this.options == null){
            return repo.find(this.filter);
        }else{
            return repo.find(this.options);
        }
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (resultIterator == null) {
            final Cursor<T> resultCusor = getQuery();
            LOG.debug("### ResultCursor initialiced with size "
                + resultCusor.size()
                + " from "
                + resultCusor.totalCount() + " for repo type: " + repo.getName());
            resultIterator = resultCusor.iterator();
        }
        if (resultIterator.hasNext()) {
            final T element = resultIterator.next();
            LOG.debug("### Gelesen: " + element);
            return element;
        } else {
            LOG.debug("### Lesen beendet für repo typ: " + repo.getName());
            repo.close();
            return null;
        }
    }

}
