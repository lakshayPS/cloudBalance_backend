package com.example.cloudBalance.controller;

import com.example.cloudBalance.service.SnowflakeService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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


    @GetMapping("/costs")
    public List<Map<String, Object>> getAllCosts() {
        return snowflakeService.getAllCosts();
    }


    @GetMapping("/charts/cost-per-service")
    public List<Map<String, Object>> getCostPerService() {
        return snowflakeService.getCostPerService();
    }

    @GetMapping("/charts/cost-per-region")
    public List<Map<String, Object>> getCostPerRegion() {
        return snowflakeService.getCostPerRegion();
    }

    @GetMapping("/charts/cost-per-instance-type")
    public List<Map<String, Object>> getCostPerInstanceType() {
        return snowflakeService.getCostPerInstanceType();
    }

    @GetMapping("/charts/cost-per-account")
    public List<Map<String, Object>> getCostPerAccount() {
        return snowflakeService.getCostPerAccount();
    }

//    @GetMapping("/charts/monthly-cost-by-group")
//    public List<Map<String, Object>> getMonthlyCostByGroup(@RequestParam(defaultValue = "SERVICE") String groupBy) {
//        return snowflakeService.getMonthlyCostByField(groupBy);
//    }
@GetMapping("/monthly-cost-by-group")
public List<Map<String, Object>> getMonthlyCost(
        @RequestParam String groupBy,
        @RequestParam int fromYear,
        @RequestParam int fromMonth,
        @RequestParam int toYear,
        @RequestParam int toMonth
) {
    return snowflakeService.getMonthlyCostByField(
            groupBy, fromYear, fromMonth, toYear, toMonth
    );
}


    /* ---------------- TIME SERIES ---------------- */

    @GetMapping("/charts/monthly-cost")
    public List<Map<String, Object>> getMonthlyCost() {
        return snowflakeService.getMonthlyCost();
    }

    @GetMapping("/charts/monthly-cost-by-service")
    public List<Map<String, Object>> getMonthlyCostByService() {
        return snowflakeService.getMonthlyCostByService();
    }

    @GetMapping("/charts/daily-cost")
    public List<Map<String, Object>> getDailyCost() {
        return snowflakeService.getDailyCost();
    }

    /* ---------------- FILTERED TABLE ---------------- */

    @GetMapping("/costs/filter")
    public List<Map<String, Object>> getCostsWithFilters(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String instanceType,
            @RequestParam(required = false) Long accountId
    ) {

        Map<String, Object> filters = new HashMap<>();

        if (service != null) {
            filters.put("SERVICE", service);
        }
        if (region != null) {
            filters.put("REGION", region);
        }
        if (instanceType != null) {
            filters.put("INSTANCE_TYPE", instanceType);
        }
        if (accountId != null) {
            filters.put("ACCOUNT_ID", accountId);
        }

        return snowflakeService.getCostsWithFilters(filters);
    }
}
