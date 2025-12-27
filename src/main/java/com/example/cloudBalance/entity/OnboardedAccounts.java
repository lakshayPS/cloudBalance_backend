package com.example.cloudBalance.entity;

import com.example.cloudBalance.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "onboarded_accounts")
public class OnboardedAccounts {

    @Id
    private Long accId;

    @Column(nullable = false)
    private String accName;

    @Column(nullable = false, unique = true)
    private String iamARN;

    @Enumerated(EnumType.STRING)
    private AccountStatus accStatus;

    @ManyToMany
    @JoinTable(
            name = "account_users",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignedToAccounts = new HashSet<>();;
}


