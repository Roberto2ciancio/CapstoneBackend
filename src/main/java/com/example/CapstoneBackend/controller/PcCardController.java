package com.example.CapstoneBackend.controller;

import com.example.CapstoneBackend.dto.PcCardDto;
import com.example.CapstoneBackend.model.PcCard;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.service.PcCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pc-cards")
public class PcCardController {

    @Autowired
    private PcCardService pcCardService;

    // CREATE
    @PostMapping
    public ResponseEntity<PcCard> createCard(@RequestBody PcCardDto cardDto, 
                                           @AuthenticationPrincipal User currentUser) {
        PcCard nuovaCard = pcCardService.createCard(cardDto, currentUser);
        return new ResponseEntity<>(nuovaCard, HttpStatus.CREATED);
    }

    // READ (tutti)
    @GetMapping
    public ResponseEntity<List<PcCard>> getAllCards() {
        return ResponseEntity.ok(pcCardService.getAllCards());
    }

    // READ (singola card)
    @GetMapping("/{id}")
    public ResponseEntity<PcCard> getCardById(@PathVariable Long id) {
        return ResponseEntity.ok(pcCardService.getCardById(id));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PcCard> updateCard(@PathVariable Long id, 
                                           @RequestBody PcCardDto cardDto,
                                           @AuthenticationPrincipal User currentUser) {
        PcCard cardAggiornata = pcCardService.updateCard(id, cardDto, currentUser);
        return ResponseEntity.ok(cardAggiornata);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id, 
                                         @AuthenticationPrincipal User currentUser) {
        pcCardService.deleteCard(id, currentUser);
        return ResponseEntity.noContent().build();
    }
}