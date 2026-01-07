package com.example.cloudBalance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String tokenType;
    private String token;
    private String userName;
    private String email;
    private String role;
}