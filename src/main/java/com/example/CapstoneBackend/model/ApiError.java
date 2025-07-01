package com.example.CapstoneBackend.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private String messaggio;
    private LocalDateTime dataErrore;
}
