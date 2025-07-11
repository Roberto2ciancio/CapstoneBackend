package com.example.CapstoneBackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "pc_cards")
public class PcCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    private double price;
    private String image;
}