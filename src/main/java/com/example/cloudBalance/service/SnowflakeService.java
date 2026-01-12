package com.example.cloudBalance.service;

import com.example.cloudBalance.repository.snowflake.SnowflakeRepository;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class SnowflakeService {

    private final SnowflakeRepository repository;

    public SnowflakeService(SnowflakeRepository repository) {
        this.repository = repository;
    }


    public List<Map<String, Object>> getAllCosts() {
        return repository.fetchAll("COST_EXPLORER.PUBLIC.COSTS");
    }


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

    public List<Map<String, Object>> getMonthlyCostByRegion() {
        return repository.fetchQuery(
                "SELECT TO_CHAR(BILL_DATE,'YYYY-MM') AS MONTH, REGION AS SERVICE, " +
                        "SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY MONTH, REGION " +
                        "ORDER BY MONTH"
        );
    }

    public List<Map<String, Object>> getMonthlyCostByInstanceType() {
        return repository.fetchQuery(
                "SELECT TO_CHAR(BILL_DATE,'YYYY-MM') AS MONTH, INSTANCE_TYPE AS SERVICE, " +
                        "SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY MONTH, INSTANCE_TYPE " +
                        "ORDER BY MONTH"
        );
    }

    public List<Map<String, Object>> getMonthlyCostByAccount() {
        return repository.fetchQuery(
                "SELECT TO_CHAR(BILL_DATE,'YYYY-MM') AS MONTH, ACCOUNT_ID AS SERVICE, " +
                        "SUM(COST) AS TOTAL_COST " +
                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
                        "GROUP BY MONTH, ACCOUNT_ID " +
                        "ORDER BY MONTH"
        );
    }


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

//    public List<Map<String, Object>> getMonthlyCostByField(String field) {
//        if (field == null || field.isEmpty()) field = "SERVICE";
//
//        List<String> validFields = List.of(
//                "SERVICE", "INSTANCE_TYPE", "ACCOUNT_ID", "USAGE_TYPE", "PLATFORM", "REGION", "USAGE_TYPE_GROUP",
//                "PURCHASE_OPTION", "API_OPERATION", "RESOURCE", "AVAILABILITY_ZONE", "TENANCY", "LEGAL_ENTITY", "BILLING_ENTITY"
//        );
//
//        if (!validFields.contains(field.toUpperCase())) {
//            field = "SERVICE"; // default
//        }
//
//        String query = String.format(
//                "SELECT TO_CHAR(BILL_DATE,'YYYY-MM') AS MONTH, %s AS GROUP_FIELD, SUM(COST) AS TOTAL_COST " +
//                        "FROM COST_EXPLORER.PUBLIC.COSTS " +
//                        "GROUP BY MONTH, %s ORDER BY MONTH",
//                field, field
//        );
//
//        return repository.fetchQuery(query);
//    }

    public List<Map<String, Object>> getMonthlyCostByField(
            String field,
            int fromYear,
            int fromMonth,
            int toYear,
            int toMonth
    ) {

        if (field == null || field.isEmpty()) field = "SERVICE";

        List<String> validFields = List.of(
                "SERVICE", "INSTANCE_TYPE", "ACCOUNT_ID", "USAGE_TYPE", "PLATFORM",
                "REGION", "USAGE_TYPE_GROUP", "PURCHASE_OPTION", "API_OPERATION",
                "RESOURCE", "AVAILABILITY_ZONE", "TENANCY", "LEGAL_ENTITY", "BILLING_ENTITY"
        );

        if (!validFields.contains(field.toUpperCase())) {
            field = "SERVICE";
        }

        // 📅 Build date range
        String fromDate = String.format("%04d-%02d-01", fromYear, fromMonth);
        String toDate = String.format(
                "%04d-%02d-%02d",
                toYear,
                toMonth,
                YearMonth.of(toYear, toMonth).lengthOfMonth()
        );

        String query = String.format(
                """
                SELECT
                    TO_CHAR(BILL_DATE, 'YYYY-MM') AS MONTH,
                    %s AS GROUP_FIELD,
                    SUM(COST) AS TOTAL_COST
                FROM COST_EXPLORER.PUBLIC.COSTS
                WHERE BILL_DATE BETWEEN '%s' AND '%s'
                GROUP BY MONTH, %s
                ORDER BY MONTH
                """,
                field, fromDate, toDate, field
        );

        return repository.fetchQuery(query);
    }


    public List<Map<String, Object>> getCostsWithFilters(Map<String, Object> filters) {
        return repository.fetchWithFilters(
                "COST_EXPLORER.PUBLIC.COSTS",
                filters
        );
    }
}
