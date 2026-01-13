package com.example.cloudBalance.dto;
import java.util.List;
import java.util.Map;

public class FullCostTableDTO {
    private List<String> months;
    private List<CostRowDTO> rows;
    private Map<String, Double> columnTotals;
    private double grandTotal;

    public FullCostTableDTO() {}

    public FullCostTableDTO(List<String> months, List<CostRowDTO> rows, Map<String, Double> columnTotals, double grandTotal) {
        this.months = months;
        this.rows = rows;
        this.columnTotals = columnTotals;
        this.grandTotal = grandTotal;
    }

    // Getters & Setters
    public List<String> getMonths() { return months; }
    public void setMonths(List<String> months) { this.months = months; }

    public List<CostRowDTO> getRows() { return rows; }
    public void setRows(List<CostRowDTO> rows) { this.rows = rows; }

    public Map<String, Double> getColumnTotals() { return columnTotals; }
    public void setColumnTotals(Map<String, Double> columnTotals) { this.columnTotals = columnTotals; }

    public double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(double grandTotal) { this.grandTotal = grandTotal; }
}
