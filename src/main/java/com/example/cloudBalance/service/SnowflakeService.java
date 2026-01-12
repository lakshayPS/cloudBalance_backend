package com.example.cloudBalance.service;

import com.example.cloudBalance.repository.snowflake.SnowflakeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SnowflakeService {

    private final SnowflakeRepository repository;

    public SnowflakeService(SnowflakeRepository repository) {
        this.repository = repository;
    }

    /* ---------------- RAW TABLE ---------------- */

    public List<Map<String, Object>> getAllCosts() {
        return repository.fetchAll("COST_EXPLORER.PUBLIC.COSTS");
    }

    /* ---------------- GROUP BY ---------------- */

    public List<Map<String, Object>> getCostPerService() {
        return repository.fetchQuery(
                "SELECT SERVICE, SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY SERVICE " +
                        "ORDER BY TOTAL_COST DESC"
        );
    }

    public List<Map<String, Object>> getCostPerRegion() {
        return repository.fetchQuery(
                "SELECT REGION, SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY REGION " +
                        "ORDER BY TOTAL_COST DESC"
        );
    }

    public List<Map<String, Object>> getCostPerInstanceType() {
        return repository.fetchQuery(
                "SELECT INSTANCE_TYPE, SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY INSTANCE_TYPE " +
                        "ORDER BY TOTAL_COST DESC"
        );
    }

    public List<Map<String, Object>> getCostPerAccount() {
        return repository.fetchQuery(
                "SELECT ACCOUNT_ID, SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY ACCOUNT_ID " +
                        "ORDER BY TOTAL_COST DESC"
        );
    }

    /* ---------------- TIME SERIES ---------------- */

    public List<Map<String, Object>> getMonthlyCost() {
        return repository.fetchQuery(
                "SELECT TO_CHAR(BILL_DATE,'YYYY-MM') AS MONTH, " +
                        "SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY MONTH " +
                        "ORDER BY MONTH"
        );
    }

    public List<Map<String, Object>> getMonthlyCostByService() {
        return repository.fetchQuery(
                "SELECT TO_CHAR(BILL_DATE,'YYYY-MM') AS MONTH, SERVICE, " +
                        "SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY MONTH, SERVICE " +
                        "ORDER BY MONTH"
        );
    }

    public List<Map<String, Object>> getDailyCost() {
        return repository.fetchQuery(
                "SELECT BILL_DATE, SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY BILL_DATE " +
                        "ORDER BY BILL_DATE"
        );
    }

    /* ---------------- FILTERED (TABLE VIEW) ---------------- */

    public List<Map<String, Object>> getCostsWithFilters(Map<String, Object> filters) {
        return repository.fetchWithFilters(
                "COST_EXPLORER.PUBLIC.COSTS",
                filters
        );
    }
}
