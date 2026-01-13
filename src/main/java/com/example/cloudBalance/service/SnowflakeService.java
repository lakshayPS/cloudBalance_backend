package com.example.cloudBalance.service;

import com.example.cloudBalance.repository.snowflake.SnowflakeRepository;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.*;

@Service
public class SnowflakeService {

    private final SnowflakeRepository repository;

    public SnowflakeService(SnowflakeRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getMonthlyCostByField(
            Long accountId,
            String field,
            int fromYear,
            int fromMonth,
            int toYear,
            int toMonth
    ) {

        if (accountId == null) {
            throw new IllegalArgumentException("AccountId is required");
        }

        if (field == null || field.isEmpty()) {
            field = "SERVICE";
        }

        List<String> validFields = List.of(
                "SERVICE", "INSTANCE_TYPE", "USAGE_TYPE", "PLATFORM",
                "REGION", "USAGE_TYPE_GROUP", "PURCHASE_OPTION", "API_OPERATION",
                "RESOURCE", "AVAILABILITY_ZONE", "TENANCY",
                "LEGAL_ENTITY", "BILLING_ENTITY"
        );

        field = field.toUpperCase();
        if (!validFields.contains(field)) {
            field = "SERVICE";
        }

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
                WHERE ACCOUNT_ID = %d
                  AND BILL_DATE BETWEEN '%s' AND '%s'
                GROUP BY MONTH, %s
                ORDER BY MONTH
                """,
                field,
                accountId,
                fromDate,
                toDate,
                field
        );

        return repository.fetchQuery(query);
    }


    private static final String[] FILTERS = {
            "SERVICE",
            "INSTANCE_TYPE",
            "ACCOUNT_ID",
            "USAGE_TYPE",
            "PLATFORM",
            "REGION",
            "USAGE_TYPE_GROUP",
            "PURCHASE_OPTION",
            "API_OPERATION",
            "RESOURCE",
            "AVAILABILITY_ZONE",
            "TENANCY",
            "LEGAL_ENTITY",
            "BILLING_ENTITY"
    };

    public Map<String, List<String>> getAllFilterOptions() {
        Map<String, List<String>> options = new LinkedHashMap<>();

        for (String filter : FILTERS) {
            String sql = String.format("SELECT DISTINCT \"%s\" FROM %s ORDER BY \"%s\"", filter, "COSTS", filter);
            List<Map<String, Object>> rows = repository.fetchQuery(sql);

            List<String> values = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                Object val = row.get(filter);
                if (val != null) values.add(val.toString());
            }

            options.put(filter, values);
        }

        return options;
    }
}
