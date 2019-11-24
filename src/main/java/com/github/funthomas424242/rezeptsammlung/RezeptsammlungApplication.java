package com.github.funthomas424242.rezeptsammlung;

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


import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Paths;

@SpringBootApplication
public class RezeptsammlungApplication {

    private static final Logger log = LoggerFactory.getLogger(RezeptsammlungApplication.class);

    protected EmbeddedSolrServer embeddedSolrServer;
    protected CoreContainer coreContainer;

    public static void main(String[] args) {
        SpringApplication.run(RezeptsammlungApplication.class, args);
    }

    @PostConstruct
    protected void startSolrServer() throws ParserConfigurationException, SAXException, IOException {
        coreContainer = CoreContainer.createAndLoad(Paths.get(".", "solrhome"));
        embeddedSolrServer = new EmbeddedSolrServer(coreContainer, "rezeptserver");
    }

    @PreDestroy
    protected void shutdownSolrServer() {
        embeddedSolrServer.getCoreContainer().shutdown();
        if (!coreContainer.isShutDown()) {
            // zoombie servers container
            coreContainer.shutdown();
        }
    }

}
