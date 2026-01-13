package com.example.cloudBalance.dto;

import java.util.Map;

public class CostRowDTO {
    private String service;
    private Map<String, Double> monthly; // month -> cost
    private double total;

    public CostRowDTO() {}

    public CostRowDTO(String service, Map<String, Double> monthly, double total) {
        this.service = service;
        this.monthly = monthly;
        this.total = total;
    }

    // Getters & Setters
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }

    public Map<String, Double> getMonthly() { return monthly; }
    public void setMonthly(Map<String, Double> monthly) { this.monthly = monthly; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}
