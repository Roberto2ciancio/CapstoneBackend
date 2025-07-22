package com.example.CapstoneBackend.security;

import com.example.CapstoneBackend.exception.NotFoundException;
import  com.example.CapstoneBackend.exception.UnAuthorizedException;
import com.example.CapstoneBackend.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        // Se l'endpoint non richiede filtro, prosegui
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new UnAuthorizedException("Token non presente, non sei autorizzato ad usare il servizio richiesto");
            }

            String token = authorization.substring(7);
            jwtTool.validateToken(token);
            User user = jwtTool.getUserFromToken(token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            filterChain.doFilter(request, response);
        } catch (UnAuthorizedException | NotFoundException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludedEndpoints = {"/auth/**", "/html/**", "/api/pc-cards/**"}; // rimosso /api/send-confirmation
        
        return Arrays.stream(excludedEndpoints)
                .anyMatch(pattern -> new AntPathMatcher().match(pattern, request.getServletPath()));
    }
}