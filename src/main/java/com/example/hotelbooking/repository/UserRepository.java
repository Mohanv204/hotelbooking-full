package com.example.hotelbooking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hotelbooking.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}

