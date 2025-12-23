package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OnboardedAcResponse {

    private Long accId;
    private String accName;
    private String iamARN;
    private AccountStatus accStatus;
    private List<String> userEmails;
}
