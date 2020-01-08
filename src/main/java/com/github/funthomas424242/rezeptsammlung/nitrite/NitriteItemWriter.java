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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class NitriteItemWriter<T> implements ItemWriter<T>, InitializingBean {
    protected static final Log LOG = LogFactory.getLog(NitriteItemWriter.class);

    protected NitriteRepository<T> repository;

    public NitriteItemWriter(final NitriteRepository<T> repository) {
        this.repository = repository;
        LOG.debug("### Nitrite Repository zugewiesen.");
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        LOG.debug("### after properties set called.");
    }

    @Override
    public void write(List<? extends T> list) throws Exception {
        LOG.debug("### Beginne mit dem Schreiben der Items ins repo:" + repository.getName());

        final Consumer<T> itemConsumer = getItemConsumer();
        final Consumer<T> itemClassLogger = getItemClassLogger("### write with item type: ");
        final Consumer<T> itemSingleLogger = getItemLogger("### write single item with data: ");
        final Consumer<T> itemListLogger = getItemLogger("### write list item with data: ");

        list.stream()
            .filter(rawItem -> rawItem instanceof List)
            .peek(itemClassLogger)
            .flatMap(items -> castList(items).stream())
            .peek(itemListLogger)
            .forEach(itemConsumer);
        list.stream()
            .filter(rawItem -> !(rawItem instanceof List))
            .peek(itemSingleLogger)
            .forEach(itemConsumer);
    }

    protected Consumer<T> getItemConsumer(){
        return  item -> repository.insert(item);
    }

    protected Consumer<T> getItemLogger(final String message){
        return  item -> LOG.debug(message + item);
    }

    protected Consumer<T> getItemClassLogger(final String message){
        return  item -> LOG.debug(message + item.getClass());
    }

    protected List<? extends T> castList(final T  item){
        // TODO no idea to fix the type cast
        final List<T> list = (List) item;
        return list.stream().filter(i->true).collect(Collectors.toList());
    }
}
