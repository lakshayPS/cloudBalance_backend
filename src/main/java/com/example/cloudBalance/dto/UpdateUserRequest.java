package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class UpdateUserRequest {

    @Email(message = "Invalid email format")
    private String email;

    private String firstName;
    private String lastName;
    private Role role;
    private List<
            @Positive(message = "Account ID must be a positive number")
                    Long
            > accountIds;
}
