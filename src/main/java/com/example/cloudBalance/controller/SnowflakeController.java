package com.example.cloudBalance.controller;

import com.example.cloudBalance.service.SnowflakeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/snowflake")
public class SnowflakeController {

    private final SnowflakeService snowflakeService;

    public SnowflakeController(SnowflakeService snowflakeService) {
        this.snowflakeService = snowflakeService;
    }

    @GetMapping("/ec2")
    public List<Map<String, Object>> getEc2() {
        return snowflakeService.getEc2Data();
    }

    @GetMapping("/rds")
    public List<Map<String, Object>> getRds() {
        return snowflakeService.getRdsData();
    }

    @GetMapping("/asg")
    public List<Map<String, Object>> getAsg() {
        return snowflakeService.getAsgData();
    }

    @GetMapping("/costs")
    public List<Map<String, Object>> getCosts() {
        return snowflakeService.getCostsData();
    }

    @GetMapping("/charts/cost-per-service")
    public List<Map<String, Object>> getCostPerService() {
        return snowflakeService.getCostPerService();
    }

    @GetMapping("/charts/cost-per-region")
    public List<Map<String, Object>> getCostPerRegion() {
        return snowflakeService.getCostPerRegion();
    }

    @GetMapping("/charts/monthly-cost")
    public List<Map<String, Object>> getMonthlyCost() {
        return snowflakeService.getMonthlyCost();
    }
}
