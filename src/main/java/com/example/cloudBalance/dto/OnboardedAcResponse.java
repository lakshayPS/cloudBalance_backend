//package com.example.cloudBalance.dto;
//
//import com.example.cloudBalance.enums.AccountStatus;
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
//public class OnboardedAcResponse {
//
//    private Long accId;
//    private String accName;
//    private AccountStatus accStatus;
//}


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
