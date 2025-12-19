package com.example.cloudBalance.controller;

import com.example.cloudBalance.dto.OnboardedAccDTO;
import com.example.cloudBalance.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<OnboardedAccDTO> createAccount(
            @RequestBody OnboardedAccDTO dto
    ) {
        return new ResponseEntity<>(
                accountService.createAccount(dto),
                HttpStatus.CREATED
        );
    }
}
