package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.AssignAccountsRequest;
import com.example.cloudBalance.dto.OnboardedAcRequest;
import com.example.cloudBalance.dto.OnboardedAcResponse;
import com.example.cloudBalance.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<OnboardedAcResponse> createAccount(
            @Valid @RequestBody OnboardedAcRequest dto
    ) {
        return new ResponseEntity<>(
                accountService.createAccount(dto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/getAllAccounts")
    public ResponseEntity<List<OnboardedAcResponse>> getAllOnboardedAccounts() {
        return ResponseEntity.ok(accountService.getAllOnboardedAccounts());
    }

    @PostMapping("/users/{userId}/assign-accounts")
    public ResponseEntity<String> assignAccountsToUser(
            @Positive(message = "User ID must be a positive number")
            @PathVariable Long userId,
            @Valid @RequestBody AssignAccountsRequest request
    ) {
        accountService.assignAccountToUser(userId, request.getAccIds());
        return ResponseEntity.ok("Accounts assigned to user successfully");
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<OnboardedAcResponse>> getAccountsByUserID(
            @Positive(message = "User ID must be a positive number")
            @PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<List<OnboardedAcResponse>> getAccountsByUserEmail(
            @RequestParam String email) {

        return ResponseEntity.ok(
                accountService.getAccountsByUserEmail(email)
        );
    }
}
