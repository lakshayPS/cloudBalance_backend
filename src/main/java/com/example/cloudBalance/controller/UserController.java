package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.RegisterRequest;
import com.example.cloudBalance.dto.UpdateUserRequest;
import com.example.cloudBalance.dto.UserResponse;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.service.AuthService;
import com.example.cloudBalance.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.registerAndAssignAccounts(request));
    }

    @PostMapping("/editUser/{userId}")
    public ResponseEntity<UserResponse> editUser(@Valid @Positive(message = "User ID must be a positive number") @PathVariable Long userId, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.editUser(userId, request));
    }
}