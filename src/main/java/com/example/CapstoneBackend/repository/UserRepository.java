package com.example.CapstoneBackend.repository;

import com.example.CapstoneBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    public Optional<User> findByUsername(String username);
}
