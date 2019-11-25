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
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SolrServerLauncher {

    private EmbeddedSolrServer embeddedSolrServer;
    protected CoreContainer coreContainer;

    public static void main(String[] args) {
        final SolrServerLauncher launcher = new SolrServerLauncher();
        try {
            launcher.startSolr();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private void startSolr() throws ParserConfigurationException, IOException, SAXException {
        //        LOG.info("### startSorlServer");
        final Path path = Paths.get("solrhome");
//        System.out.println("###"+path.toAbsolutePath().toString());
//        System.out.println("###>"+path.resolve("solr.xml").toAbsolutePath().toString());
//        coreContainer = CoreContainer.createAndLoad(path);
//        System.out.println("###"+path.toAbsolutePath().toString());
//        embeddedSolrServer = new EmbeddedSolrServer(coreContainer, "rezeptserver");
        System.out.println("### start");
        final EmbeddedSolrServerFactory factory = new EmbeddedSolrServerFactory(path.toAbsolutePath().toString());
        System.out.println("### start1");
//        this.embeddedSolrServer = factory.createPathConfiguredSolrServer(path.toAbsolutePath().toString());
        this.embeddedSolrServer = factory.getSolrClient();
        System.out.println("### start2");
        this.coreContainer = embeddedSolrServer.getCoreContainer();
        System.out.println("### start ended");
//        LOG.info("### startSorlServer geschafft");
    }
}
