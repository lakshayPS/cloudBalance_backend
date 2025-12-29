package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.Role;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {

    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private List<Long> accountIds;
}
