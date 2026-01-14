package com.example.cloudBalance.enums;

import java.util.Arrays;

public enum GroupField {
    SERVICE,
    INSTANCE_TYPE,
    USAGE_TYPE,
    PLATFORM,
    REGION,
    USAGE_TYPE_GROUP,
    PURCHASE_OPTION,
    API_OPERATION,
    RESOURCE,
    AVAILABILITY_ZONE,
    TENANCY,
    LEGAL_ENTITY,
    BILLING_ENTITY;

    public static GroupField from(String value) {
        if (value == null || value.isBlank()) {
            return SERVICE; // default
        }

        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(SERVICE);
    }
}
