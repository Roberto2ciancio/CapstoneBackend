package com.example.CapstoneBackend.controller;

import com.example.CapstoneBackend.dto.LoginDto;
import com.example.CapstoneBackend.dto.UserDto;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.exception.ValidationException;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.service.AuthService;
import com.example.CapstoneBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;


    @PostMapping("/auth/register")
    public User register(@RequestBody @Validated UserDto userDto, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (s,e)->s+e));
        }
        return userService.saveUser(userDto);
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody @Validated LoginDto loginDto, BindingResult bindingResult) throws ValidationException {
        if(bindingResult.hasErrors()){
            throw new ValidationException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (s,e)->s+e));
        }

        try {
            return authService.login(loginDto);
        } catch (NotFoundException e) {
            throw new ValidationException("Credenziali non valide");
        }
    }
}
