package com.example.CapstoneBackend.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "pc_cards")
public class PcCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String brand;
    
    @Column(nullable = false)
    private String model;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer stockQuantity;
    
    private String imageUrl;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    // Technical specifications
    private String memory;
    private String memoryType;
    private String coreClock;
    private String boostClock;
    private String powerConsumption;
    private String connectors;
    private String dimensions;
}