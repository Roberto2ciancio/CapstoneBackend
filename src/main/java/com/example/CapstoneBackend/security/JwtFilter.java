package com.example.CapstoneBackend.security;

import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.exception.UnAuthorizedException;
import com.example.CapstoneBackend.model.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private final JwtTool jwtTool;

    public JwtFilter(JwtTool jwtTool) {
        this.jwtTool = jwtTool;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Controlla prima se non dovrebbe filtrare
            if (shouldNotFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            String authorization = request.getHeader("Authorization");

            if (authorization == null || !authorization.startsWith("Bearer ")) {
                throw new UnAuthorizedException("Token non presente, non sei autorizzato ad usare il servizio richiesto");
            }
            
            //estraggo il token
            String token = authorization.substring(7);

            //verifico che il token sia valido
            jwtTool.validateToken(token);

            //recupero l'utente collegato al token usando il metodo getUserFromToken del jwtTool
            User user = jwtTool.getUserFromToken(token);

            //creo un oggetto authentication inserendogli all'interno l'utente recuperato e il suo ruolo
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            //aggiungo l'autenticazione con l'utente nel contesto di Spring security
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
        String[] excludedEndpoints = new String[]{"/auth/**", "/html/**"};

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }
}