package com.example.cloudBalance.dto;

import java.util.List;
import java.util.Map;

public class FilterResponse {
    private Map<String, List<String>> options;

    public FilterResponse() {}

    public FilterResponse(Map<String, List<String>> options) {
        this.options = options;
    }

    public Map<String, List<String>> getOptions() {
        return options;
    }

    public void setOptions(Map<String, List<String>> options) {
        this.options = options;
    }
}
