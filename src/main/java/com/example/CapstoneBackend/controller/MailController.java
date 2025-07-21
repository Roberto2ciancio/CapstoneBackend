package com.example.CapstoneBackend.controller;

import com.example.CapstoneBackend.dto.EmailDto;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.service.MailService;
import com.example.CapstoneBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MailController {
    
    @Value("${env.gmail.mail.from}")
    private String mittente;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/send-confirmation")
    public ResponseEntity<String> sendConfirmationEmail(@RequestBody EmailDto emailDto) {
        try {
            // Ottieni l'utente autenticato
            User utenteAutenticato = userService.getAuthenticatedUser();
            
            if (utenteAutenticato == null || utenteAutenticato.getEmail() == null) {
                return ResponseEntity.badRequest().body("Utente non autenticato o email non trovata");
            }
            
            // Validazione
            if (emailDto.getNumeroOrdine() == null || emailDto.getNumeroOrdine().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Il numero dell'ordine è obbligatorio");
            }

            String oggetto = "🎉 Ordine confermato – Grazie per aver scelto Pcstore!";
        
            String testoEmail = String.format("""
                Ciao %s,
                
                ti confermiamo che abbiamo ricevuto il tuo ordine #%s e lo stiamo preparando con cura.
                
                🛠️ Spedizione:
                Il tuo PC verrà spedito entro 3-5 giorni lavorativi. Riceverai una seconda email con il codice di tracciamento non appena il pacco sarà affidato al corriere.
                
                Grazie per aver scelto Pcstore per il tuo nuovo PC!
                Per qualsiasi informazione o supporto, puoi rispondere a questa email o contattarci su +39 123456789.
                
                Buona giornata e buon utilizzo del tuo nuovo PC!
                
                Il team Pcstore
                info@pcstore.it
                www.pcstore.it
                """, 
                utenteAutenticato.getNome(), 
                emailDto.getNumeroOrdine().trim()
            );

            mailService.send(mittente, utenteAutenticato.getEmail(), oggetto, testoEmail);
            return ResponseEntity.ok("Email di conferma ordine inviata con successo");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                .body("Errore nell'invio dell'email: " + e.getMessage());
        }
    }
}