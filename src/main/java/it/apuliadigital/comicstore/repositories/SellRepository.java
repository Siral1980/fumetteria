package it.apuliadigital.comicstore.repositories;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Sell;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {

    // Task 11 - ricerca per range di date
    List<Sell> findBySellingDateBetween(LocalDateTime start, LocalDateTime end);

    // Task 11 - ricerca per importo superiore
    List<Sell> findByTotalAmountGreaterThan(BigDecimal amount);
}