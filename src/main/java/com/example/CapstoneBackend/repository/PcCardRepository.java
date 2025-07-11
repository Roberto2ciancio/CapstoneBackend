package com.example.CapstoneBackend.repository;

import com.example.CapstoneBackend.model.PcCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PcCardRepository extends JpaRepository<PcCard, Long> {
}