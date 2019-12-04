package com.github.funthomas424242.rezeptsammlung.crawler;

import com.github.funthomas424242.rezeptsammlung.rezept.Rezept;
import com.github.funthomas424242.rezeptsammlung.rezept.RezeptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");

            jdbcTemplate.query("SELECT first_name, last_name FROM people",
                (rs, row) -> new RezeptBuilder().withTitel(rs.getString(1)).build()
            ).forEach(rezept -> log.info("Found <" + rezept + "> in the database."));
        }
    }
}
