package com.example.CapstoneBackend.dto;

import com.example.CapstoneBackend.enumeration.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty(message = "Obbligatorio inserire nome")
    private String nome;
    @NotEmpty(message = "Obbligatorio inserire cognome")
    private String cognome;
    @Email(message = "Obbligatorio inserire email (indirizzo@epicode.it)")
    private String email;
    @NotEmpty(message = "Obbligatorio inserire username")
    private String username;
    @NotEmpty(message = "Obbligatorio inserire password")
    private String password;


}
