package com.packge.manager.tosam.br.libraryApi.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    private static final int MAXIMUM_POOL_SIZE = 10;
    private static final int MINIMUM_IDLE_CONNECTIONS = 1;
    private static final long MAXIMUM_LIFETIME_MILLIS = 1_800_000;
    private static final long CONNECTION_TIMEOUT_MILLIS = 120_000;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        config.setMinimumIdle(MINIMUM_IDLE_CONNECTIONS);
        config.setPoolName("libraryapi-pool-db");
        config.setMaxLifetime(MAXIMUM_LIFETIME_MILLIS);
        config.setConnectionTimeout(CONNECTION_TIMEOUT_MILLIS);
        config.setConnectionTestQuery("select 1");
        return new HikariDataSource(config);
    }
}
