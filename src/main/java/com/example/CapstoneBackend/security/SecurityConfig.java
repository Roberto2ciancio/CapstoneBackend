package com.example.CapstoneBackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        //formLogin serve per creare in automatico una pagina di login. A noi non serve,perchè non usiamo pagine
        httpSecurity.formLogin(http->http.disable());
        //csrf serve per evitare la possibilità di utilizzi di sessioni aperte, ma i rest non usano sessioni e quindi disable
        httpSecurity.csrf(http->http.disable());
        httpSecurity.sessionManagement(http->http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //serve per bloccare richieste che provengono da domini(indirizzo ip e porta) esterni a quelli del servizio
        httpSecurity.cors(Customizer.withDefaults());

        // Add custom access denied and authentication entry point handlers
        httpSecurity.exceptionHandling(exceptions -> exceptions
            .accessDeniedHandler((request, response, accessDeniedException) -> {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"messaggio\":\"Accesso negato: non hai i permessi necessari per accedere a questa risorsa\",\"dataErrore\":\"" + LocalDateTime.now() + "\"}");
            })
            .authenticationEntryPoint((request, response, authException) -> {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"messaggio\":\"Token di autenticazione mancante o non valido\",\"dataErrore\":\"" + LocalDateTime.now() + "\"}");
            })
        );

        // Add JWT filter before the UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Public endpoints
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/auth/**").permitAll());
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/import/**").permitAll());
        
        // Allow public GET requests for PC cards (shop browsing)
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers(HttpMethod.GET, "/api/pcCards/**").permitAll());
        
        // Debug endpoints (temporary - for testing)
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/debug/whoami").permitAll());
        
        // Admin-only endpoints for PC cards management
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/pcCards/admin/**").hasAuthority("ADMIN"));
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/debug/admin-test").hasAuthority("ADMIN"));
        httpSecurity.authorizeHttpRequests(http->http.requestMatchers("/api/debug/user-test").hasAuthority("USER"));
        
        // Any other request requires authentication
        httpSecurity.authorizeHttpRequests(http->http.anyRequest().authenticated());

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
