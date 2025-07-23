package com.example.CapstoneBackend.repositories;

import com.example.CapstoneBackend.entities.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {
    // Qui puoi aggiungere query personalizzate se necessario
}