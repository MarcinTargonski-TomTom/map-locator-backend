package com.tomtom.locator.map.map_locator.config;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public abstract class BaseIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.4")
            .withDatabaseName("map_locator")
            .withUsername("admin")
            .withPassword("admin")
            .withReuse(true);

    static {
        postgres.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.admin.username", postgres::getUsername);
        registry.add("spring.datasource.admin.password", postgres::getPassword);
    }
}