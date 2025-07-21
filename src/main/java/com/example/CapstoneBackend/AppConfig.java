package com.example.CapstoneBackend;

import com.cloudinary.Cloudinary;
import com.example.CapstoneBackend.security.JwtFilter;
import com.example.CapstoneBackend.security.JwtTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@PropertySource("application.properties")//dove prendere le proprietà
public class AppConfig {
    @Bean

    public Cloudinary getCloudinary(@Value("${cloudinary.cloud_name}") String cloudName,
                                    @Value("${cloudinary.api_key}") String apiKey,
                                    @Value("${cloudinary.api_secret}") String apiSecret){
        Map<String, String> configCloudinary = new HashMap<>();
        configCloudinary.put("cloud_name",cloudName);
        configCloudinary.put("api_key", apiKey);
        configCloudinary.put("api_secret",apiSecret);

        return new Cloudinary(configCloudinary);
    }

@Bean
public JavaMailSenderImpl getJavaMailSender(
    @Value("${spring.mail.username}") String username,
    @Value("${spring.mail.password}") String password) {
    
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setUsername(username);
    mailSender.setPassword(password);

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "true");

    return mailSender;
}

    @Bean(name = "provinceCsvFile")
    public File provinceCsvFile() {
        return new File("src/main/resources/province-italiane.csv");
    }

    @Bean(name = "comuniCsvFile")
    public File comuniCsvFile() {
        return new File("src/main/resources/comuni-italiani.csv");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Instead of using setAllowedOrigins with "*", we use setAllowedOriginPatterns
        // This allows all origins while still allowing credentials
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Allow all methods
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")); // Allow all headers
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply to all paths
        return source;
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}