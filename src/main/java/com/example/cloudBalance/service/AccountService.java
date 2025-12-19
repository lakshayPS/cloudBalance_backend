package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.OnboardedAccDTO;
import com.example.cloudBalance.entity.OnboardedAccounts;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.enums.AccountStatus;
import com.example.cloudBalance.repository.OnboardedAccountRepository;
import com.example.cloudBalance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final OnboardedAccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public OnboardedAccDTO createAccount(OnboardedAccDTO dto) {

        if (accountRepository.existsByIamARN(dto.getIamARN())) {
            throw new IllegalStateException("Account already onboarded");
        }

        OnboardedAccounts account = new OnboardedAccounts();
        account.setAccName(dto.getAccName());
        account.setIamARN(dto.getIamARN());
        account.setAccStatus(AccountStatus.ORPHANED);
        account.setAccId(dto.getAccId());

        if (dto.getUserEmails() != null && !dto.getUserEmails().isEmpty()) {

            List<User> users = userRepository.findByEmailIn(dto.getUserEmails());

            if (users.size() != dto.getUserEmails().size()) {
                throw new IllegalArgumentException("One or more user emails are invalid");
            }

            account.setAssignedToAccounts(users);
        }

        OnboardedAccounts saved = accountRepository.save(account);

        OnboardedAccDTO response = new OnboardedAccDTO();
        response.setAccId(saved.getAccId());
        response.setAccName(saved.getAccName());
        response.setIamARN(saved.getIamARN());
        response.setAccStatus(saved.getAccStatus());

        response.setUserEmails(
                saved.getAssignedToAccounts() == null
                        ? List.of()
                        : saved.getAssignedToAccounts()
                        .stream()
                        .map(User::getEmail)
                        .toList()
        );

        return response;
    }

}
