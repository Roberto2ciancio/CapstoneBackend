package com.example.CapstoneBackend.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Username che ricevono automaticamente il ruolo ADMIN.
 * Configurati con la variabile d'ambiente ADMIN_USERNAMES (lista separata da virgole).
 */
@Component
public class AdminUsernames {

    private final Set<String> usernames;

    public AdminUsernames(@Value("${admin.usernames:}") String raw) {
        this.usernames = Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    public boolean isAdmin(String username) {
        return username != null && usernames.contains(username);
    }
}
