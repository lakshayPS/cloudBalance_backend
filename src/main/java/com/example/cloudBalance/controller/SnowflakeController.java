package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.FilterResponse;
import com.example.cloudBalance.service.SnowflakeService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/snowflake")
public class SnowflakeController {

    private final SnowflakeService snowflakeService;

    public SnowflakeController(SnowflakeService snowflakeService) {
        this.snowflakeService = snowflakeService;
    }

    @GetMapping("/monthly-cost-by-group")
    public List<Map<String, Object>> getMonthlyCost(
            @RequestParam Long accountId,
            @RequestParam String groupBy,
            @RequestParam int fromYear,
            @RequestParam int fromMonth,
            @RequestParam int toYear,
            @RequestParam int toMonth
    ) {
        return snowflakeService.getMonthlyCostByField(
                accountId, groupBy, fromYear, fromMonth, toYear, toMonth
        );
    }

    @GetMapping("/get-filters")
    public FilterResponse getFilters() {
        return new FilterResponse(snowflakeService.getAllFilterOptions());
    }
}
