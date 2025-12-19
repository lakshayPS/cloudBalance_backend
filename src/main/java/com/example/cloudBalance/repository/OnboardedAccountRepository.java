package com.example.cloudBalance.repository;

import com.example.cloudBalance.entity.OnboardedAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnboardedAccountRepository extends JpaRepository<OnboardedAccounts, Long> {

    boolean existsByIamARN(String iamARN);
// Optional: fetch account with assigned users
    // @EntityGraph(attributePaths = "assignedToAccounts")
    // Optional<OnboardedAccounts> findByAccId(Long accId);
}
