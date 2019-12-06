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
import com.github.funthomas424242.sbstarter.nitrite.NitriteRepository;
import com.github.funthomas424242.sbstarter.nitrite.NitriteTemplate;
import org.dizitart.no2.IndexType;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.net.URI;

import static org.dizitart.no2.IndexOptions.indexOptions;

@Configuration
@EnableBatchProcessing
//public class CrawlerConfiguration extends DefaultBatchConfigurer {
//@Override
//public void setDataSource(DataSource dataSource) {
// no data source used, so empty statement
//    }

public class CrawlerConfiguration {

    @Autowired
    NitriteTemplate nitriteTemplate;

    protected NitriteRepository<Rezept> repository;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    protected NitriteRepository<Rezept> getRepository() {
        final NitriteRepository<Rezept> repository = nitriteTemplate.getRepository(Rezept.class);
        // Da Indizes permanent in der Datenbank gespeichert werden,
        // dürfen diese nur 1x angelegt werden.
        // können aber async erstellt werden, da nur mit geringen Datenmengen gerechnet wird.
        if (!repository.hasIndex("id")) {
            repository.createIndex("id", indexOptions(IndexType.Unique, true));
        }
        if (!repository.hasIndex("titel")) {
            repository.createIndex("titel", indexOptions(IndexType.Fulltext, true));
        }
        if (!repository.hasIndex("tag")) {
            repository.createIndex("tag", indexOptions(IndexType.NonUnique, true));
        }
        return repository;
    }

    @Bean
    public FlatFileItemReader<Rezept> reader() {


        return new FlatFileItemReaderBuilder<Rezept>()
            .name("rezeptItemReader")
            .resource(new FileSystemResource("docs/Apfelkuchen.rezept"))
            .delimited()
            .names(new String[]{"titel", "tags"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Rezept>() {{
                setTargetType(Rezept.class);
            }})
            .build();
    }

    @Bean
    public RezeptItemProcessor processor() {
        return new RezeptItemProcessor();
    }

    @Bean
    public NitriteItemWriter<Rezept> writer() {
        final NitriteRepository<Rezept> rezeptRepo = getRepository();

        return new NitriteItemWriter<Rezept>();
    }


//    @Bean
//    public JdbcBatchItemWriter<Rezept> writer(DataSource dataSource) {
//       final NitriteRepository<Rezept> rezeptRepo = getRepository();
//
//        return new JdbcBatchItemWriterBuilder<Rezept>()
//            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
//            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
//            .dataSource(dataSource)
//            .build();
//    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importRezeptJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(NitriteItemWriter<Rezept> writer) {
        return stepBuilderFactory.get("step1")
            .<Rezept, Rezept>chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

}
