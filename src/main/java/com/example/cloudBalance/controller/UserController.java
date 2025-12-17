package com.example.cloudBalance.controller;


import com.example.cloudBalance.dto.UpdateUserRequest;
import com.example.cloudBalance.dto.UserResponse;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        System.out.println("giving users data!!!!!!!!");
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PutMapping("/updateUser")
    public ResponseEntity<User> update(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(authService.updateUser(request));
    }
}
