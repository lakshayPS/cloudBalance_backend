package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.AccountStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class OnboardedAcRequest {

    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be a positive number")
    private Long accId;

    @NotBlank(message = "Account name is required")
    private String accName;

    @NotBlank(message = "IAM ARN is required")
    private String iamARN;

    @NotNull(message = "Account status must be provided")
    private AccountStatus accStatus = AccountStatus.ORPHANED;

    private List<
            @Email(message = "Invalid user email format")
                    String
            > userEmails;
}
