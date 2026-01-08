package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.OnboardedAcRequest;
import com.example.cloudBalance.dto.OnboardedAcResponse;
import com.example.cloudBalance.entity.OnboardedAccounts;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.enums.AccountStatus;
import com.example.cloudBalance.exception.ResourceAlreadyExistsException;
import com.example.cloudBalance.exception.ResourceNotFoundException;
import com.example.cloudBalance.repository.OnboardedAccountRepository;
import com.example.cloudBalance.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final OnboardedAccountRepository accountRepository;
    private final UserRepository userRepository;

//    @Transactional
//    public OnboardedAcResponse createAccount(OnboardedAcRequest dto) {
//
//        if (accountRepository.existsById(dto.getAccId())) {
//            throw new IllegalStateException(
//                    "Account with ID " + dto.getAccId() + " already exists"
//            );
//        }
//
//        if (accountRepository.existsByIamARN(dto.getIamARN())) {
//            throw new IllegalStateException("Account already onboarded with this IAM ARN");
//        }
//
//        OnboardedAccounts account = new OnboardedAccounts();
//        account.setAccId(dto.getAccId());
//        account.setAccName(dto.getAccName());
//        account.setIamARN(dto.getIamARN());
//        account.setAccStatus(
//                dto.getAccStatus() != null ? dto.getAccStatus() : AccountStatus.ORPHANED
//        );
//
//        if (dto.getUserEmails() != null && !dto.getUserEmails().isEmpty()) {
//
//            List<User> users = userRepository.findByEmailIn(dto.getUserEmails());
//
//            if (users.size() != dto.getUserEmails().size()) {
//                throw new IllegalArgumentException("One or more user emails are invalid");
//            }
//
//            account.setAssignedToAccounts(users);
//        }
//
//        OnboardedAccounts saved = accountRepository.save(account);
//
//        OnboardedAcResponse response = new OnboardedAcResponse();
//        response.setAccId(saved.getAccId());
//        response.setAccName(saved.getAccName());
//        response.setIamARN(saved.getIamARN());
//        response.setAccStatus(saved.getAccStatus());
//        response.setUserEmails(
//                saved.getAssignedToAccounts() == null
//                        ? List.of()
//                        : saved.getAssignedToAccounts()
//                        .stream()
//                        .map(User::getEmail)
//                        .toList()
//        );
//
//        return response;
//    }

    @Transactional
    public OnboardedAcResponse createAccount(OnboardedAcRequest dto) {

        if (accountRepository.existsById(dto.getAccId())) {
            throw new ResourceAlreadyExistsException("Account already exists");
        }

        if (accountRepository.existsByIamARN(dto.getIamARN())) {
            throw new ResourceAlreadyExistsException("Account already onboarded");
        }

        OnboardedAccounts account = new OnboardedAccounts();
        account.setAccId(dto.getAccId());
        account.setAccName(dto.getAccName());
        account.setIamARN(dto.getIamARN());

        account.setAccStatus(AccountStatus.ORPHANED);

        OnboardedAccounts saved = accountRepository.save(account);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public List<OnboardedAcResponse> getAllOnboardedAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private OnboardedAcResponse mapToResponse(OnboardedAccounts ac) {
        OnboardedAcResponse dto = new OnboardedAcResponse();
        dto.setAccId(ac.getAccId());
        dto.setAccName(ac.getAccName());
        dto.setIamARN(ac.getIamARN());
        dto.setAccStatus(ac.getAccStatus());

        dto.setUserEmails(
                ac.getAssignedToAccounts() == null
                        ? List.of()
                        : ac.getAssignedToAccounts()
                        .stream()
                        .map(User::getEmail)
                        .toList()
        );

        return dto;
    }

    @Transactional
    public void assignAccountToUser(Long userId, List<Long> accIds) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<OnboardedAccounts> accounts = accountRepository.findAllById(accIds);

        if (accounts.size() != accIds.size()) {
            throw new ResourceNotFoundException("Some accounts not found");
        }

        for (OnboardedAccounts account : accounts) {

            if (!account.getAssignedToAccounts().contains(user)) {
                account.getAssignedToAccounts().add(user);
            }

            account.setAccStatus(AccountStatus.ASSIGNED);
        }

        accountRepository.saveAll(accounts);
    }


    @Transactional
    public List<OnboardedAcResponse> getAccountsByUserId(Long userId) {

        if(!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found with this id: " + userId);
        }

        List<OnboardedAccounts> accounts = accountRepository.findByAssignedToAccounts_Id(userId);

        return accounts.stream().map(this :: mapToResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<OnboardedAcResponse> getAccountsByUserEmail(String email) {

        List<OnboardedAccounts> accounts =
                accountRepository.findByAssignedToAccounts_Email(email);

        if (accounts.isEmpty()) {
            throw new IllegalArgumentException(
                    "No accounts assigned to user with email: " + email
            );
        }

        return accounts.stream()
                .map(this::mapToResponse)
                .toList();
    }

}
