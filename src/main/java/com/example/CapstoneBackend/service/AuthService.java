package com.example.CapstoneBackend.service;

import com.example.CapstoneBackend.dto.LoginDto;
import com.example.CapstoneBackend.dto.LoginResponseDto;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.repository.UserRepository;
import com.example.CapstoneBackend.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new NotFoundException("Utente con questo username/password non trovato"));

        if(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            LoginResponseDto response = new LoginResponseDto();
            response.setToken(jwtTool.createToken(user));
            response.setUsername(user.getUsername());
            response.setRuolo(user.getRuolo());
            response.setNome(user.getNome());
            response.setCognome(user.getCognome());
            response.setAvatar(user.getAvatar());
            
            return response;
        } else {
            throw new NotFoundException("Utente con questo username/password non trovato");
        }
    }
}