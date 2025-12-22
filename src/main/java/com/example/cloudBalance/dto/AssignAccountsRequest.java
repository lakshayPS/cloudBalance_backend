package com.example.cloudBalance.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AssignAccountsRequest {

    @NotEmpty
    private List<Long> accIds;
}
