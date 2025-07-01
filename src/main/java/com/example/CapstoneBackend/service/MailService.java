package com.example.CapstoneBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void send(String mittente, String destinatario, String oggetto, String testo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mittente); 
        message.setTo(destinatario);
        message.setSubject(oggetto);
        message.setText(testo);
        mailSender.send(message);
    }
}
