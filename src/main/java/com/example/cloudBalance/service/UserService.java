package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.RegisterRequest;
import com.example.cloudBalance.dto.UpdateUserRequest;
import com.example.cloudBalance.dto.UserResponse;
import com.example.cloudBalance.entity.OnboardedAccounts;
import com.example.cloudBalance.entity.User;
import com.example.cloudBalance.enums.AccountStatus;
import com.example.cloudBalance.enums.Role;
import com.example.cloudBalance.exception.EmailAlreadyInUseException;
import com.example.cloudBalance.exception.ResourceNotFoundException;
import com.example.cloudBalance.exception.UserAlreadyExistsException;
import com.example.cloudBalance.repository.mysql.OnboardedAccountRepository;
import com.example.cloudBalance.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OnboardedAccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ADMIN', 'READONLY')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return mapToUserResponseDto(users);
    }

    private List<UserResponse> mapToUserResponseDto(List<User> users) {
        List<UserResponse> result = new ArrayList<>();

        for(User user: users) {
            UserResponse userResponse = new UserResponse();
            userResponse.setId(user.getId());
            userResponse.setFirstName(user.getFirstName());
            userResponse.setLastName(user.getLastName());
            userResponse.setEmail(user.getEmail());
            userResponse.setRole(user.getRole());

            result.add(userResponse);
        }
        return result;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse registerAndAssignAccounts(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        User savedUser = userRepository.save(user);

        if (request.getRole() == Role.ROLE_CUSTOMER
                && request.getAccountIds() != null
                && !request.getAccountIds().isEmpty()) {

            List<OnboardedAccounts> accounts =
                    accountRepository.findAllById(request.getAccountIds());

            if (accounts.size() != request.getAccountIds().size()) {
                throw new ResourceNotFoundException("Some accounts not found");
            }

            for (OnboardedAccounts account : accounts) {
                assignUserToAccount(savedUser, account);
            }

            accountRepository.saveAll(accounts);
        }

        return mapToUserResponseDto(savedUser);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse editUser(Long userId, UpdateUserRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyInUseException("Email already in use");
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(request.getRole());

        userRepository.save(user);

        List<OnboardedAccounts> currentlyAssignedAccounts =
                accountRepository.findByAssignedToAccounts_Id(userId);

        if (request.getRole() != Role.ROLE_CUSTOMER) {
            unassignUserFromAccounts(user, currentlyAssignedAccounts);
            accountRepository.saveAll(currentlyAssignedAccounts);
            return mapToUserResponseDto(user);
        }

        List<Long> requestedAccountIds =
                request.getAccountIds() != null ? request.getAccountIds() : List.of();

        List<OnboardedAccounts> requestedAccounts =
                accountRepository.findAllById(requestedAccountIds);

        if (requestedAccounts.size() != requestedAccountIds.size()) {
            throw new ResourceNotFoundException("Some accounts not found");
        }

        for (OnboardedAccounts account : currentlyAssignedAccounts) {
            if (!requestedAccountIds.contains(account.getAccId())) {
                unassignUserFromAccount(user, account);
            }
        }

        for (OnboardedAccounts account : requestedAccounts) {
            assignUserToAccount(user, account);
        }

        accountRepository.saveAll(currentlyAssignedAccounts);
        accountRepository.saveAll(requestedAccounts);

        return mapToUserResponseDto(user);
    }

    private void unassignUserFromAccounts(User user, List<OnboardedAccounts> accounts) {
        for (OnboardedAccounts account : accounts) {
            unassignUserFromAccount(user, account);
        }
    }

    private void unassignUserFromAccount(User user, OnboardedAccounts account) {
        account.getAssignedToAccounts().remove(user);

        if (account.getAssignedToAccounts().isEmpty()) {
            account.setAccStatus(AccountStatus.ORPHANED);
        }
    }

    private void assignUserToAccount(User user, OnboardedAccounts account) {
        if (!account.getAssignedToAccounts().contains(user)) {
            account.getAssignedToAccounts().add(user);
            account.setAccStatus(AccountStatus.ASSIGNED);
        }
    }

    private UserResponse mapToUserResponseDto(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());

        return dto;
    }
}

