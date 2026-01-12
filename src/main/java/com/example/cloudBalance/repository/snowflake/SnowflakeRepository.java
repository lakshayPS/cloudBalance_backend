//package com.example.cloudBalance.repository.snowflake;
//
//import com.snowflake.snowpark_java.DataFrame;
//import com.snowflake.snowpark_java.Row;
//import com.snowflake.snowpark_java.Session;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public class SnowflakeRepository {
//
//    private final Session snowflakeSession;
//
//    public SnowflakeRepository(Session snowflakeSession) {
//        this.snowflakeSession = snowflakeSession;
//    }
//
//    // Fetch all rows from a table
//    public List<Map<String, Object>> getAll(String tableName) {
//        DataFrame df = snowflakeSession.table(tableName);
//        return collectDataFrame(df);
//    }
//
//    // Execute a custom SQL query
//    public List<Map<String, Object>> queryForList(String sql) {
//        DataFrame df = snowflakeSession.sql(sql);
//        return collectDataFrame(df);
//    }
//
//    // Convert DataFrame rows to List<Map<String,Object>>
//    private List<Map<String, Object>> collectDataFrame(DataFrame df) {
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        // Column names
//        String[] columnNames = df.columns();
//
//        // Collect rows
//        List<Row> rows = df.collect();
//        for (Row row : rows) {
//            Map<String, Object> rowMap = new HashMap<>();
//            for (int i = 0; i < columnNames.length; i++) {
//                rowMap.put(columnNames[i], row.getColumnValue(i));
//            }
//            result.add(rowMap);
//        }
//
//        return result;
//    }
//}


//package com.example.cloudBalance.repository.snowflake;
//
//import com.snowflake.snowpark_java.DataFrame;
//import com.snowflake.snowpark_java.Row;
//import com.snowflake.snowpark_java.Session;
////import com.snowflake.snowpark_java.types.StructField;
//import org.springframework.stereotype.Repository;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import com.snowflake.snowpark_java.DataFrame;
//import com.snowflake.snowpark_java.Row;
//import com.snowflake.snowpark_java.types.StructField;
//import com.snowflake.snowpark_java.types.StructType;
//
//@Repository
//public class SnowflakeRepository {
//
//    private final Session snowflakeSession;
//
//    public SnowflakeRepository(Session snowflakeSession) {
//        this.snowflakeSession = snowflakeSession;
//    }
//
//    /** Fetch all rows from a table */
//    public List<Map<String, Object>> fetchAll(String tableName) {
//        return fetchData(snowflakeSession.table(tableName));
//    }
//
//    /** Execute a SQL query and fetch results */
//    public List<Map<String, Object>> fetchQuery(String sql) {
//        return fetchData(snowflakeSession.sql(sql));
//    }
//
//    /** Generic method to convert DataFrame to List<Map<String,Object>> */
//    private List<Map<String, Object>> fetchData(DataFrame df) {
//        // Parse column names from schema string
//        String schemaStr = df.schema().toString(); // e.g., "STRUCT<ID:INT, NAME:STRING>"
//        String cols = schemaStr.substring(schemaStr.indexOf("<") + 1, schemaStr.indexOf(">"));
//        String[] columnNames = Arrays.stream(cols.split(","))
//                .map(s -> s.split(":")[0].trim())
//                .toArray(String[]::new);
//
//        Row[] rows = df.collect();
//        List<Map<String, Object>> result = new ArrayList<>();
//
//        for (Row row : rows) {
//            Map<String, Object> rowMap = new HashMap<>();
//            for (int i = 0; i < columnNames.length; i++) {
//                // Use row.get(int) to get the value
//                rowMap.put(columnNames[i], row.get(i));
//            }
//            result.add(rowMap);
//        }
//
//        return result;
//    }
//
//    /** Optional: Execute parameterized query with filters */
//    public List<Map<String, Object>> fetchWithFilters(String tableName, Map<String, Object> filters) {
//        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(tableName);
//        if (filters != null && !filters.isEmpty()) {
//            sql.append(" WHERE ");
//            List<String> conditions = new ArrayList<>();
//            filters.forEach((key, value) -> conditions.add(key + " = '" + value + "'"));
//            sql.append(String.join(" AND ", conditions));
//        }
//        return fetchQuery(sql.toString());
//    }
//}

package com.example.cloudBalance.repository.snowflake;

import com.snowflake.snowpark.SnowparkClientException;
import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Row;
import com.snowflake.snowpark_java.Session;
import org.springframework.stereotype.Repository;

import java.util.*;
import com.snowflake.snowpark_java.DataFrame;
import com.snowflake.snowpark_java.Row;
import com.snowflake.snowpark_java.Session;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class SnowflakeRepository {

    private final Session snowflakeSession;

    public SnowflakeRepository(Session snowflakeSession) {
        this.snowflakeSession = snowflakeSession;
    }

    /** Fetch all rows from a table */
    public List<Map<String, Object>> fetchAll(String tableName) {
        DataFrame df = snowflakeSession.table(tableName);
        return toList(df);
    }

    /** Execute raw SQL */
    public List<Map<String, Object>> fetchQuery(String sql) {
        DataFrame df = snowflakeSession.sql(sql);
        return toList(df);
    }

    /** Convert DataFrame → List<Map<String,Object>> */
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

    /** Optional filters */
    public List<Map<String, Object>> fetchWithFilters(String tableName, Map<String, Object> filters) {

        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(tableName);

        if (filters != null && !filters.isEmpty()) {
            sql.append(" WHERE ");
            List<String> conditions = new ArrayList<>();

            filters.forEach((k, v) ->
                    conditions.add(k + " = '" + v + "'")
            );

            sql.append(String.join(" AND ", conditions));
        }

        return fetchQuery(sql.toString());
    }
}
