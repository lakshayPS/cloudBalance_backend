//package com.example.cloudBalance.dto;
//
//import com.example.cloudBalance.enums.AccountStatus;
//import lombok.Data;
//
//import java.util.List;
//
//import static com.example.cloudBalance.enums.AccountStatus.ORPHANED;
//
//@Data
//public class OnboardedAcRequest {
//
//    private Long accId;
//    private String accName;
//    private String iamARN;
//    private AccountStatus accStatus = ORPHANED;
//    private List<String> userEmails;
//}

package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OnboardedAcRequest {

    @NotNull(message = "Account ID is required")
    private Long accId;

    @NotBlank(message = "Account name is required")
    private String accName;

    @NotBlank(message = "IAM ARN is required")
    private String iamARN;

    private AccountStatus accStatus = AccountStatus.ORPHANED;

    private List<String> userEmails;
}
