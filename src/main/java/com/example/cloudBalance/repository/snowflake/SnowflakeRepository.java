package com.example.cloudBalance.repository.snowflake;

import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Row;
import com.snowflake.snowpark_java.Session;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.*;

@Repository
public class SnowflakeRepository {

    private final Session snowflakeSession;

    public SnowflakeRepository(Session snowflakeSession) {
        this.snowflakeSession = snowflakeSession;
    }

    public List<Map<String, Object>> fetchQuery(String sql) {
        DataFrame df = snowflakeSession.sql(sql);
        return toList(df);
    }

    private List<Map<String, Object>> toList(DataFrame df) {

        Row[] rows = df.collect();
        String[] columnNames = df.schema().names();

        List<Map<String, Object>> result = new ArrayList<>();

        for (Row row : rows) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < columnNames.length; i++) {
                map.put(columnNames[i], row.get(i));
            }
            result.add(map);
        }
        return result;
    }
}
