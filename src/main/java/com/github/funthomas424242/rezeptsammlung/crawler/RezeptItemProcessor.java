package com.github.funthomas424242.rezeptsammlung.crawler;

import com.github.funthomas424242.rezeptsammlung.rezept.Rezept;
import com.github.funthomas424242.rezeptsammlung.rezept.RezeptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class RezeptItemProcessor implements ItemProcessor<URI, Rezept> {

    private static final Logger log = LoggerFactory.getLogger(RezeptItemProcessor.class);

    @Override
    public Rezept process(final URI uri) throws Exception {

        // Lies JSON from uri
        // erzeuge Rezept from JSON
        final String titel = "Das beste im Kuchen";
        final List<String> tags = Arrays.asList("vegan","mit Fleisch");

        final Rezept rezeptNew = new RezeptBuilder().withTitel(titel).withTags(tags).build();

        log.info("Converting (" + uri + ") into (" + rezeptNew + ")");

        return rezeptNew;
    }

}
