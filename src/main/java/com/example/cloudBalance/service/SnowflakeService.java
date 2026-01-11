package com.example.cloudBalance.service;

import com.example.cloudBalance.repository.SnowflakeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SnowflakeService {

    private final SnowflakeRepository repository;

    public SnowflakeService(SnowflakeRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> getEc2Data() {
        return repository.getAll("EC2_SERVICES");
    }

    public List<Map<String, Object>> getRdsData() {
        return repository.getAll("RDS_SERVICES");
    }

    public List<Map<String, Object>> getAsgData() {
        return repository.getAll("ASG_SERVICES");
    }

    public List<Map<String, Object>> getCostsData() {
        return repository.getAll("AWS_COSTS");
    }

    public List<Map<String, Object>> getCostPerService() {
        String sql = "SELECT SERVICE, SUM(COST) AS TOTAL_COST FROM AWS_COSTS GROUP BY SERVICE ORDER BY TOTAL_COST DESC";
        return repository.queryForList(sql);
    }

    public List<Map<String, Object>> getCostPerRegion() {
        String sql = "SELECT REGION, SUM(COST) AS TOTAL_COST FROM AWS_COSTS GROUP BY REGION ORDER BY TOTAL_COST DESC";
        return repository.queryForList(sql);
    }

    public List<Map<String, Object>> getMonthlyCost() {
        String sql = "SELECT TO_CHAR(USAGE_DATE, 'YYYY-MM') AS MONTH, SUM(COST) AS TOTAL_COST FROM AWS_COSTS GROUP BY MONTH ORDER BY MONTH ASC";
        return repository.queryForList(sql);
    }
}
