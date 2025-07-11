package com.example.CapstoneBackend.service;

import com.example.CapstoneBackend.dto.PcCardDto;
import com.example.CapstoneBackend.model.PcCard;
import com.example.CapstoneBackend.model.User;
import com.example.CapstoneBackend.enumeration.Ruolo;
import com.example.CapstoneBackend.repository.PcCardRepository;
import com.example.CapstoneBackend.exception.UnAuthorizedException;
import com.example.CapstoneBackend.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PcCardService {
    
    @Autowired
    private PcCardRepository pcCardRepository;
    
    private void verificaAdmin(User user) {
        if (user.getRuolo() != Ruolo.ADMIN) {
            throw new UnAuthorizedException("Solo gli amministratori possono modificare le card");
        }
    }

    // Create
    public PcCard createCard(PcCardDto cardDto, User currentUser) {
        verificaAdmin(currentUser);
        
        PcCard card = new PcCard();
        return salvaCard(card, cardDto);
    }

    // Read (tutti)
    public List<PcCard> getAllCards() {
        return pcCardRepository.findAll();
    }

    // Read (singola card)
    public PcCard getCardById(Long id) {
        return pcCardRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Card non trovata con id: " + id));
    }

    // Update
    public PcCard updateCard(Long id, PcCardDto cardDto, User currentUser) {
        verificaAdmin(currentUser);
        
        PcCard esistente = pcCardRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Card non trovata con id: " + id));
            
        return salvaCard(esistente, cardDto);
    }

    // Delete
    public void deleteCard(Long id, User currentUser) {
        verificaAdmin(currentUser);
        
        if (!pcCardRepository.existsById(id)) {
            throw new NotFoundException("Card non trovata con id: " + id);
        }
        pcCardRepository.deleteById(id);
    }

    private PcCard salvaCard(PcCard card, PcCardDto dto) {
        card.setName(dto.getName());
        card.setDescription(dto.getDescription());
        card.setPrice(dto.getPrice());
        card.setImage(dto.getImage());
        return pcCardRepository.save(card);
    }
}