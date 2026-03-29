package com.securetrade.repository;

import com.securetrade.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// only need one custom method here - find user by username during login

public interface UserRepository extends JpaRepository<User, Long> {

    // used in AuthController to check if user exists during login
    Optional<User> findByUsername(String username);
}