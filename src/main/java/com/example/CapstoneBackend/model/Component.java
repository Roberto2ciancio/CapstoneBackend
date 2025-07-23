package com.example.CapstoneBackend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "components")
public class Component {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String category; // CPU, GPU, RAM, ecc.
    private String brand;
    private String model;
    private Double price;
    private String description;
    private Boolean available = true;
}