package com.tomtom.locator.map.map_locator.config;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class LiquibaseRunner {

    private static final Logger logger = LoggerFactory.getLogger(LiquibaseRunner.class);

    private final DataSource dataSource;

    @EventListener
    @Order(1000)
    public void onApplicationReady(ApplicationReadyEvent event) {
        logger.info("Running Liquibase after application startup...");

        try (Connection connection = dataSource.getConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.yaml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update(new Contexts(), new LabelExpression());
            logger.info("Liquibase update completed successfully");

        } catch (Exception e) {
            logger.error("Error running Liquibase: ", e);
            throw new IllegalStateException("Failed to run Liquibase", e);
        }
    }
}
