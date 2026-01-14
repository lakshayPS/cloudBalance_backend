package com.example.cloudBalance.service;

import com.example.cloudBalance.enums.GroupField;
import com.example.cloudBalance.repository.snowflake.SnowflakeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SnowflakeService {

    private static final String COSTS_TABLE = "COSTS";
    private final SnowflakeRepository repository;

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

        GroupField groupField = GroupField.from(field);
        String fieldName = groupField.name();

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
                FROM COST_EXPLORER.PUBLIC.%s
                WHERE ACCOUNT_ID = %d
                  AND BILL_DATE BETWEEN '%s' AND '%s'
                GROUP BY MONTH, %s
                ORDER BY MONTH
                """,
                fieldName,
                COSTS_TABLE,
                accountId,
                fromDate,
                toDate,
                fieldName
        );

        return repository.fetchQuery(query);
    }

    public Map<String, List<String>> getAllFilterOptions() {
        Map<String, List<String>> options = new LinkedHashMap<>();

        for (GroupField filter : GroupField.values()) {
            String field = filter.name();

            String sql = String.format(
                    "SELECT DISTINCT \"%s\" FROM %s ORDER BY \"%s\"",
                    field,
                    COSTS_TABLE,
                    field
            );

            List<Map<String, Object>> rows = repository.fetchQuery(sql);

            List<String> values = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                Object val = row.get(field);
                if (val != null) {
                    values.add(val.toString());
                }
            }

            options.put(field, values);
        }

        return options;
    }
}
