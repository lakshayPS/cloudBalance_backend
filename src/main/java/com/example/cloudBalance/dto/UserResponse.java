package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.Role;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}
