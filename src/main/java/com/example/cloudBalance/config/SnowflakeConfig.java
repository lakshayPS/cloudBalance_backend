package com.example.cloudBalance.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class SnowflakeConfig {

    @Value("${snowflake.url}")
    private String jdbcUrl;

    @Value("${snowflake.username}")
    private String username;

    @Value("${snowflake.password}")
    private String password;

    @Value("${snowflake.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource snowflakeDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName(driverClassName);
        return ds;
    }

    @Bean
    public JdbcTemplate snowflakeJdbcTemplate(@Qualifier("snowflakeDataSource") DataSource ds) {
        return new JdbcTemplate(ds);
    }
}

