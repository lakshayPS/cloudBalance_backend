package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.AssignAccountsRequest;
import com.example.cloudBalance.dto.OnboardedAcRequest;
import com.example.cloudBalance.dto.OnboardedAcResponse;
import com.example.cloudBalance.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
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
            @PathVariable Long userId,
            @RequestBody AssignAccountsRequest request
    ) {
        accountService.assignAccountToUser(userId, request.getAccIds());
        return ResponseEntity.ok("Accounts assigned to user successfully");
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<OnboardedAcResponse>> getAccountsByUser (@PathVariable Long userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

}
