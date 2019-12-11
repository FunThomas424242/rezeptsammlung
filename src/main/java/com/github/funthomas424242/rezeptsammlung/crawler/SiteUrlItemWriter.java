//package com.github.funthomas424242.rezeptsammlung.crawler;
//
///*-
// * #%L
// * rezeptsammlung
// * %%
// * Copyright (C) 2019 PIUG
// * %%
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as
// * published by the Free Software Foundation, either version 3 of the
// * License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Lesser Public License for more details.
// *
// * You should have received a copy of the GNU General Lesser Public
// * License along with this program.  If not, see
// * <http://www.gnu.org/licenses/lgpl-3.0.html>.
// * #L%
// */
//
//import com.github.funthomas424242.rezeptsammlung.nitrite.NitriteItemWriter;
//import com.github.funthomas424242.sbstarter.nitrite.NitriteRepository;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.InitializingBean;
//
//import java.util.List;
//
//public class SiteUrlItemWriter<T> implements ItemWriter<T>, InitializingBean {
//    protected static final Logger LOG = LoggerFactory.getLogger(SiteUrlItemWriter.class);
//    protected NitriteRepository<T> repository;
//
//    public SiteUrlItemWriter(final NitriteRepository<T> repository) {
//        this.repository = repository;
//        LOG.debug("Nitrite Repository zugewiesen.");
//    }
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        LOG.debug("after properties set called.");
//    }
//
//    @Override
//    public void write(List<? extends T> list) throws Exception {
//        LOG.debug("### LIST: "+list);
//        list.forEach(
//            o -> {
//                final SiteUrl siteUrl = (SiteUrl) o;
//                LOG.debug("### siteUrl: "+siteUrl);
////                if (siteUrl.url.endsWith(".rezept")) {
////                    siteUrl.siteType = SiteType.REZEPT_URL;
////                } else {
////                    siteUrl.siteType = SiteType.SITE_URL;
////                }
//                repository.insert(o);
//            });
//    }
//}
