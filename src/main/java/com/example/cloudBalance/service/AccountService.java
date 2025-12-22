//package com.example.cloudBalance.service;
//
//import com.example.cloudBalance.dto.OnboardedAcRequest;
//import com.example.cloudBalance.dto.OnboardedAcResponse;
//import com.example.cloudBalance.entity.OnboardedAccounts;
//import com.example.cloudBalance.entity.User;
//import com.example.cloudBalance.enums.AccountStatus;
//import com.example.cloudBalance.repository.OnboardedAccountRepository;
//import com.example.cloudBalance.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AccountService {
//
//    private final OnboardedAccountRepository accountRepository;
//    private final UserRepository userRepository;
//
//    @Transactional
//    public OnboardedAcRequest createAccount(OnboardedAcRequest dto) {
//
//        if (accountRepository.existsByIamARN(dto.getIamARN())) {
//            throw new IllegalStateException("Account already onboarded");
//        }
//
//        OnboardedAccounts account = new OnboardedAccounts();
//        account.setAccName(dto.getAccName());
//        account.setIamARN(dto.getIamARN());
//        account.setAccStatus(AccountStatus.ORPHANED);
//        account.setAccId(dto.getAccId());
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
//        OnboardedAcRequest response = new OnboardedAcRequest();
//        response.setAccId(saved.getAccId());
//        response.setAccName(saved.getAccName());
//        response.setIamARN(saved.getIamARN());
//        response.setAccStatus(saved.getAccStatus());
//
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
//
//    public List<OnboardedAcResponse> getAllOnboardedAccounts() {
//        List<OnboardedAccounts> accounts = accountRepository.findAll();
//        return mapToResponseDTO(accounts);
//    }
//
//    private List<OnboardedAcResponse> mapToResponseDTO(List<OnboardedAccounts> accounts) {
//        List<OnboardedAcResponse> response = new ArrayList<>();
//
//        for(OnboardedAccounts ac: accounts) {
//            OnboardedAcResponse responseDto = new OnboardedAcResponse();
//            responseDto.setAccId(ac.getAccId());
//            responseDto.setAccName(ac.getAccName());
//            responseDto.setAccStatus(ac.getAccStatus());
//
//            response.add(responseDto);
//        }
//
//        System.out.println("res: " + response);
//        return response;
//    }
//
//}


package com.example.cloudBalance.service;

import com.example.cloudBalance.dto.OnboardedAcRequest;
import com.example.cloudBalance.dto.OnboardedAcResponse;
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
    public OnboardedAcResponse createAccount(OnboardedAcRequest dto) {

        // 1️⃣ Validate accId uniqueness
        if (accountRepository.existsById(dto.getAccId())) {
            throw new IllegalStateException(
                    "Account with ID " + dto.getAccId() + " already exists"
            );
        }

        // 2️⃣ Validate IAM ARN uniqueness
        if (accountRepository.existsByIamARN(dto.getIamARN())) {
            throw new IllegalStateException("Account already onboarded with this IAM ARN");
        }

        // 3️⃣ Map DTO → Entity
        OnboardedAccounts account = new OnboardedAccounts();
        account.setAccId(dto.getAccId());
        account.setAccName(dto.getAccName());
        account.setIamARN(dto.getIamARN());
        account.setAccStatus(
                dto.getAccStatus() != null ? dto.getAccStatus() : AccountStatus.ORPHANED
        );

        // 4️⃣ Assign users (if provided)
        if (dto.getUserEmails() != null && !dto.getUserEmails().isEmpty()) {

            List<User> users = userRepository.findByEmailIn(dto.getUserEmails());

            if (users.size() != dto.getUserEmails().size()) {
                throw new IllegalArgumentException("One or more user emails are invalid");
            }

            account.setAssignedToAccounts(users);
        }

        // 5️⃣ Save
        OnboardedAccounts saved = accountRepository.save(account);

        // 6️⃣ Map Entity → Response DTO
        OnboardedAcResponse response = new OnboardedAcResponse();
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

//    public List<OnboardedAcResponse> getAllOnboardedAccounts() {
//        return accountRepository.findAll()
//                .stream()
//                .map(this::mapToResponse)
//                .toList();
//    }

    @Transactional(readOnly = true)
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
    public void assignAccountsToUser(Long userId, List<Long> accIds) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<OnboardedAccounts> accounts = accountRepository.findAllById(accIds);

        if (accounts.size() != accIds.size()) {
            throw new IllegalArgumentException("One or more accounts not found");
        }

        for (OnboardedAccounts account : accounts) {

            if (!account.getAssignedToAccounts().contains(user)) {
                account.getAssignedToAccounts().add(user);
            }

            // Status update
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
}
