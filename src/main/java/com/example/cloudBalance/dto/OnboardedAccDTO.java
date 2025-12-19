package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.AccountStatus;
import lombok.Data;

import java.util.List;

import static com.example.cloudBalance.enums.AccountStatus.ORPHANED;

@Data
public class OnboardedAccDTO {

    private Long accId;
    private String accName;
    private String iamARN;
    private AccountStatus accStatus = ORPHANED;
    private List<String> userEmails;
}
