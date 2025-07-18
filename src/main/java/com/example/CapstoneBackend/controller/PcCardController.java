package com.example.CapstoneBackend.controller;

import com.example.CapstoneBackend.dto.PcCardDto;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.exception.ValidationException;
import com.example.CapstoneBackend.model.PcCard;
import com.example.CapstoneBackend.service.PcCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pcCards")
public class PcCardController {
    
    @Autowired
    private PcCardService pcCardService;
    
    // Public endpoints - accessible to everyone
    
    @GetMapping
    public ResponseEntity<List<PcCard>> getAllPcCards() {
        return ResponseEntity.ok(pcCardService.getAvailablePcCards());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PcCard> getPcCardById(@PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(pcCardService.getPcCardById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<PcCard>> searchPcCards(@RequestParam String q) {
        return ResponseEntity.ok(pcCardService.searchPcCards(q));
    }
    
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<PcCard>> getPcCardsByBrand(@PathVariable String brand) {
        return ResponseEntity.ok(pcCardService.getPcCardsByBrand(brand));
    }
    
    @GetMapping("/price-range")
    public ResponseEntity<List<PcCard>> getPcCardsByPriceRange(
            @RequestParam BigDecimal minPrice, 
            @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(pcCardService.getPcCardsByPriceRange(minPrice, maxPrice));
    }
    
    // Admin only endpoints
    
    @GetMapping("/admin/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PcCard>> getAllPcCardsForAdmin() {
        return ResponseEntity.ok(pcCardService.getAllPcCards());
    }
    
    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PcCard> createPcCard(
            @RequestBody @Validated PcCardDto pcCardDto, 
            BindingResult bindingResult) throws ValidationException {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (s, e) -> s + e));
        }
        
        PcCard createdCard = pcCardService.createPcCard(pcCardDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }
    
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<PcCard> updatePcCard(
            @PathVariable Long id,
            @RequestBody @Validated PcCardDto pcCardDto,
            BindingResult bindingResult) throws ValidationException, NotFoundException {
        
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream()
                    .map(objectError -> objectError.getDefaultMessage())
                    .reduce("", (s, e) -> s + e));
        }
        
        PcCard updatedCard = pcCardService.updatePcCard(id, pcCardDto);
        return ResponseEntity.ok(updatedCard);
    }
    
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletePcCard(@PathVariable Long id) throws NotFoundException {
        pcCardService.deletePcCard(id);
        return ResponseEntity.noContent().build();
    }
}