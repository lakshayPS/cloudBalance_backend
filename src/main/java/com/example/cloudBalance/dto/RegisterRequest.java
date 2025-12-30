package com.example.cloudBalance.dto;

import com.example.cloudBalance.enums.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 4, message = "Password must be at least 8 characters")
    private String password;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotNull(message = "Role must be specified")
    private Role role;

    private List<
            @Positive(message = "Account ID must be a positive number")
                    Long
            > accountIds;
}
