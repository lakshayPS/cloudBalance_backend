package com.example.cloudBalance.repository.mysql;

import com.example.cloudBalance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<User, Long> {
    public User findByEmail(String email);
    public Boolean existsByEmail(String email);
}

