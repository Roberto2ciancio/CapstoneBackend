package com.example.CapstoneBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void send(String from, String to, String subject, String text) {
        // Validazione dei parametri
        if (to == null || to.trim().isEmpty()) {
            throw new IllegalArgumentException("L'indirizzo email del destinatario non può essere vuoto");
        }
        if (from == null || from.trim().isEmpty()) {
            throw new IllegalArgumentException("L'indirizzo email del mittente non può essere vuoto");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("L'oggetto dell'email non può essere vuoto");
        }
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Il contenuto dell'email non può essere vuoto");
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from.trim());
            message.setTo(to.trim());
            message.setSubject(subject.trim());
            message.setText(text);
            
            System.out.println("Tentativo di invio email a: " + to);
            mailSender.send(message);
            System.out.println("Email inviata con successo");
        } catch (MailException e) {
            System.err.println("Errore nell'invio dell'email: " + e.getMessage());
            throw new RuntimeException("Errore durante l'invio dell'email: " + e.getMessage(), e);
        }
    }
}