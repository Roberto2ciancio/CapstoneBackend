package com.example.CapstoneBackend.controller;

import com.example.CapstoneBackend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/whoami")
    public ResponseEntity<Map<String, Object>> whoami() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        if (auth != null) {
            response.put("authenticated", auth.isAuthenticated());
            response.put("principal", auth.getPrincipal());
            response.put("authorities", auth.getAuthorities());
            response.put("name", auth.getName());
            
            if (auth.getPrincipal() instanceof User) {
                User user = (User) auth.getPrincipal();
                response.put("userId", user.getId());
                response.put("username", user.getUsername());
                response.put("role", user.getRuolo());
            }
        } else {
            response.put("message", "No authentication found");
        }
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin-test")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Map<String, String>> adminTest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin access successful!");
        response.put("endpoint", "/api/debug/admin-test");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user-test")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, String>> userTest() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User access successful!");
        response.put("endpoint", "/api/debug/user-test");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/any-auth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> anyAuth() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Any authenticated user access successful!");
        response.put("endpoint", "/api/debug/any-auth");
        return ResponseEntity.ok(response);
    }
}