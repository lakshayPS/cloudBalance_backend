package com.example.cloudBalance.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class AssignAccountsRequest {

    @NotEmpty(message = "Account IDs list cannot be empty")
    private List<
            @NotNull(message = "Account ID cannot be null")
            @Positive(message = "Account ID must be a positive number")
                    Long
            > accIds;
}
