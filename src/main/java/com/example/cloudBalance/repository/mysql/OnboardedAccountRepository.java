package com.example.cloudBalance.repository.mysql;

import com.example.cloudBalance.entity.OnboardedAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OnboardedAccountRepository extends JpaRepository<OnboardedAccounts, Long> {

    boolean existsByIamARN(String iamARN);
    List<OnboardedAccounts> findByAssignedToAccounts_Id(Long userId);
    List<OnboardedAccounts> findByAssignedToAccounts_Email(String email);
// Optional: fetch account with assigned users
    // @EntityGraph(attributePaths = "assignedToAccounts")
    // Optional<OnboardedAccounts> findByAccId(Long accId);
}
