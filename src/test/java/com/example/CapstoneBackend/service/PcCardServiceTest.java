package com.example.CapstoneBackend.service;

import com.example.CapstoneBackend.dto.PcCardDto;
import com.example.CapstoneBackend.exception.NotFoundException;
import com.example.CapstoneBackend.model.PcCard;
import com.example.CapstoneBackend.repository.PcCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PcCardServiceTest {

    @Mock
    private PcCardRepository pcCardRepository;

    @InjectMocks
    private PcCardService pcCardService;

    private PcCard testPcCard;
    private PcCardDto testPcCardDto;

    @BeforeEach
    void setUp() {
        testPcCard = new PcCard();
        testPcCard.setId(1L);
        testPcCard.setName("RTX 4070");
        testPcCard.setBrand("NVIDIA");
        testPcCard.setModel("RTX 4070");
        testPcCard.setDescription("High-performance gaming graphics card");
        testPcCard.setPrice(new BigDecimal("599.99"));
        testPcCard.setStockQuantity(10);
        testPcCard.setAvailable(true);
        testPcCard.setMemory("12GB");
        testPcCard.setMemoryType("GDDR6X");

        testPcCardDto = new PcCardDto();
        testPcCardDto.setName("RTX 4070");
        testPcCardDto.setBrand("NVIDIA");
        testPcCardDto.setModel("RTX 4070");
        testPcCardDto.setDescription("High-performance gaming graphics card");
        testPcCardDto.setPrice(new BigDecimal("599.99"));
        testPcCardDto.setStockQuantity(10);
        testPcCardDto.setAvailable(true);
        testPcCardDto.setMemory("12GB");
        testPcCardDto.setMemoryType("GDDR6X");
    }

    @Test
    void testGetAllPcCards() {
        List<PcCard> expectedCards = Arrays.asList(testPcCard);
        when(pcCardRepository.findAll()).thenReturn(expectedCards);

        List<PcCard> result = pcCardService.getAllPcCards();

        assertEquals(expectedCards, result);
        verify(pcCardRepository, times(1)).findAll();
    }

    @Test
    void testGetAvailablePcCards() {
        List<PcCard> expectedCards = Arrays.asList(testPcCard);
        when(pcCardRepository.findByAvailableTrue()).thenReturn(expectedCards);

        List<PcCard> result = pcCardService.getAvailablePcCards();

        assertEquals(expectedCards, result);
        verify(pcCardRepository, times(1)).findByAvailableTrue();
    }

    @Test
    void testGetPcCardById_Success() throws NotFoundException {
        when(pcCardRepository.findById(1L)).thenReturn(Optional.of(testPcCard));

        PcCard result = pcCardService.getPcCardById(1L);

        assertEquals(testPcCard, result);
        verify(pcCardRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPcCardById_NotFound() {
        when(pcCardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pcCardService.getPcCardById(1L));
        verify(pcCardRepository, times(1)).findById(1L);
    }

    @Test
    void testCreatePcCard() {
        when(pcCardRepository.save(any(PcCard.class))).thenReturn(testPcCard);

        PcCard result = pcCardService.createPcCard(testPcCardDto);

        assertNotNull(result);
        assertEquals(testPcCard.getName(), result.getName());
        assertEquals(testPcCard.getBrand(), result.getBrand());
        assertEquals(testPcCard.getPrice(), result.getPrice());
        verify(pcCardRepository, times(1)).save(any(PcCard.class));
    }

    @Test
    void testUpdatePcCard_Success() throws NotFoundException {
        when(pcCardRepository.findById(1L)).thenReturn(Optional.of(testPcCard));
        when(pcCardRepository.save(any(PcCard.class))).thenReturn(testPcCard);

        PcCard result = pcCardService.updatePcCard(1L, testPcCardDto);

        assertNotNull(result);
        verify(pcCardRepository, times(1)).findById(1L);
        verify(pcCardRepository, times(1)).save(any(PcCard.class));
    }

    @Test
    void testUpdatePcCard_NotFound() {
        when(pcCardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pcCardService.updatePcCard(1L, testPcCardDto));
        verify(pcCardRepository, times(1)).findById(1L);
        verify(pcCardRepository, never()).save(any(PcCard.class));
    }

    @Test
    void testDeletePcCard_Success() throws NotFoundException {
        when(pcCardRepository.findById(1L)).thenReturn(Optional.of(testPcCard));

        assertDoesNotThrow(() -> pcCardService.deletePcCard(1L));
        verify(pcCardRepository, times(1)).findById(1L);
        verify(pcCardRepository, times(1)).delete(testPcCard);
    }

    @Test
    void testDeletePcCard_NotFound() {
        when(pcCardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> pcCardService.deletePcCard(1L));
        verify(pcCardRepository, times(1)).findById(1L);
        verify(pcCardRepository, never()).delete(any(PcCard.class));
    }

    @Test
    void testSearchPcCards() {
        List<PcCard> expectedCards = Arrays.asList(testPcCard);
        when(pcCardRepository.searchByNameOrBrand("RTX")).thenReturn(expectedCards);

        List<PcCard> result = pcCardService.searchPcCards("RTX");

        assertEquals(expectedCards, result);
        verify(pcCardRepository, times(1)).searchByNameOrBrand("RTX");
    }

    @Test
    void testGetPcCardsByBrand() {
        List<PcCard> expectedCards = Arrays.asList(testPcCard);
        when(pcCardRepository.findByBrandIgnoreCase("NVIDIA")).thenReturn(expectedCards);

        List<PcCard> result = pcCardService.getPcCardsByBrand("NVIDIA");

        assertEquals(expectedCards, result);
        verify(pcCardRepository, times(1)).findByBrandIgnoreCase("NVIDIA");
    }

    @Test
    void testGetPcCardsByPriceRange() {
        BigDecimal minPrice = new BigDecimal("500.00");
        BigDecimal maxPrice = new BigDecimal("700.00");
        List<PcCard> expectedCards = Arrays.asList(testPcCard);
        when(pcCardRepository.findByPriceRange(minPrice, maxPrice)).thenReturn(expectedCards);

        List<PcCard> result = pcCardService.getPcCardsByPriceRange(minPrice, maxPrice);

        assertEquals(expectedCards, result);
        verify(pcCardRepository, times(1)).findByPriceRange(minPrice, maxPrice);
    }
}