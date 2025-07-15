package com.example.CapstoneBackend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class PcCardDto {
    @NotEmpty(message = "Il nome è obbligatorio")
    @Size(max = 255, message = "Il nome non può superare 255 caratteri")
    private String name;
    
    @Size(max = 1000, message = "La descrizione non può superare 1000 caratteri")
    private String description;
    
    @Size(max = 500, message = "L'URL dell'immagine non può superare 500 caratteri")
    private String image;
    
    @NotNull(message = "Il prezzo è obbligatorio")
    @Min(value = 0, message = "Il prezzo non può essere negativo")
    private BigDecimal price;
}