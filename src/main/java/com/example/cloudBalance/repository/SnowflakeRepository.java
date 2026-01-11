package com.example.cloudBalance.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class SnowflakeRepository {

    private final JdbcTemplate jdbcTemplate;

    public SnowflakeRepository(@Qualifier("snowflakeJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> getAll(String tableName) {
        return jdbcTemplate.queryForList("SELECT * FROM " + tableName);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }
}
