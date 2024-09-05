package com.dmitrenko.testcontainertest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public JdbcDatabaseContainer<?> jdbcDatabaseContainer() {
        return new PostgreSQLContainer<>("postgres:latest");
    }

    @Bean
    @Primary
    public DataSource testDataSource(JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcDatabaseContainer.getJdbcUrl());
        hikariConfig.setUsername(jdbcDatabaseContainer.getUsername());
        hikariConfig.setPassword(jdbcDatabaseContainer.getPassword());

        return new HikariDataSource(hikariConfig);
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
