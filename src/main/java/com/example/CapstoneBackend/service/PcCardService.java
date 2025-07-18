package com.example.CapstoneBackend.service;

import com.example.CapstoneBackend.dto.PcCardDto;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.model.PcCard;
import com.example.CapstoneBackend.repository.PcCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PcCardService {
    
    @Autowired
    private PcCardRepository pcCardRepository;
    
    // Get all PC cards
    public List<PcCard> getAllPcCards() {
        return pcCardRepository.findAll();
    }
    
    // Get available PC cards only
    public List<PcCard> getAvailablePcCards() {
        return pcCardRepository.findByAvailableTrue();
    }
    
    // Get PC card by ID
    public PcCard getPcCardById(Long id) throws NotFoundException {
        return pcCardRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("PC Card with ID " + id + " not found"));
    }
    
    // Create new PC card (Admin only)
    public PcCard createPcCard(PcCardDto pcCardDto) {
        PcCard pcCard = convertDtoToEntity(pcCardDto);
        return pcCardRepository.save(pcCard);
    }
    
    // Update existing PC card (Admin only)
    public PcCard updatePcCard(Long id, PcCardDto pcCardDto) throws NotFoundException {
        PcCard existingCard = getPcCardById(id);
        
        // Update fields
        existingCard.setName(pcCardDto.getName());
        existingCard.setBrand(pcCardDto.getBrand());
        existingCard.setModel(pcCardDto.getModel());
        existingCard.setDescription(pcCardDto.getDescription());
        existingCard.setPrice(pcCardDto.getPrice());
        existingCard.setStockQuantity(pcCardDto.getStockQuantity());
        existingCard.setImageUrl(pcCardDto.getImageUrl());
        existingCard.setAvailable(pcCardDto.getAvailable());
        existingCard.setMemory(pcCardDto.getMemory());
        existingCard.setMemoryType(pcCardDto.getMemoryType());
        existingCard.setCoreClock(pcCardDto.getCoreClock());
        existingCard.setBoostClock(pcCardDto.getBoostClock());
        existingCard.setPowerConsumption(pcCardDto.getPowerConsumption());
        existingCard.setConnectors(pcCardDto.getConnectors());
        existingCard.setDimensions(pcCardDto.getDimensions());
        
        return pcCardRepository.save(existingCard);
    }
    
    // Delete PC card (Admin only)
    public void deletePcCard(Long id) throws NotFoundException {
        PcCard pcCard = getPcCardById(id);
        pcCardRepository.delete(pcCard);
    }
    
    // Search PC cards
    public List<PcCard> searchPcCards(String searchTerm) {
        return pcCardRepository.searchByNameOrBrand(searchTerm);
    }
    
    // Get PC cards by brand
    public List<PcCard> getPcCardsByBrand(String brand) {
        return pcCardRepository.findByBrandIgnoreCase(brand);
    }
    
    // Get PC cards by price range
    public List<PcCard> getPcCardsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return pcCardRepository.findByPriceRange(minPrice, maxPrice);
    }
    
    // Convert DTO to Entity
    private PcCard convertDtoToEntity(PcCardDto dto) {
        PcCard pcCard = new PcCard();
        pcCard.setName(dto.getName());
        pcCard.setBrand(dto.getBrand());
        pcCard.setModel(dto.getModel());
        pcCard.setDescription(dto.getDescription());
        pcCard.setPrice(dto.getPrice());
        pcCard.setStockQuantity(dto.getStockQuantity());
        pcCard.setImageUrl(dto.getImageUrl());
        pcCard.setAvailable(dto.getAvailable());
        pcCard.setMemory(dto.getMemory());
        pcCard.setMemoryType(dto.getMemoryType());
        pcCard.setCoreClock(dto.getCoreClock());
        pcCard.setBoostClock(dto.getBoostClock());
        pcCard.setPowerConsumption(dto.getPowerConsumption());
        pcCard.setConnectors(dto.getConnectors());
        pcCard.setDimensions(dto.getDimensions());
        return pcCard;
    }
}