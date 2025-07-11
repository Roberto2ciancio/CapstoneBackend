package com.example.CapstoneBackend.dto;

import com.example.CapstoneBackend.enumeration.Ruolo;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String username;
    private Ruolo ruolo;
    private String nome;
    private String cognome;
    private String avatar;
}