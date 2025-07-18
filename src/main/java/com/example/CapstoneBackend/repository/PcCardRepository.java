package com.example.CapstoneBackend.repository;

import com.example.CapstoneBackend.model.PcCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PcCardRepository extends JpaRepository<PcCard, Long> {
    
    // Find all available PC cards
    List<PcCard> findByAvailableTrue();
    
    // Find PC cards by brand
    List<PcCard> findByBrandIgnoreCase(String brand);
    
    // Find PC cards within price range
    @Query("SELECT p FROM PcCard p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<PcCard> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                  @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    // Search PC cards by name or brand
    @Query("SELECT p FROM PcCard p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<PcCard> searchByNameOrBrand(@Param("searchTerm") String searchTerm);
}