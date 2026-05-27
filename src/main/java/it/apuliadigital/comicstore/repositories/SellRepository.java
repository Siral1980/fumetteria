package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {
    
    // Cerca usando "sellingDate"
    List<Sell> findBySellingDateBetween(LocalDateTime start, LocalDateTime end);

    // Cerca usando "totalAmount"
    List<Sell> findByTotalAmountGreaterThan(BigDecimal price);
}