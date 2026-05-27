package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {

    // Vendite in un range di date
    List<Sell> findBySellingDateBetween(LocalDateTime from, LocalDateTime to);

    // Vendite con totale superiore ad un importo
    List<Sell> findByTotalAmountGreaterThan(BigDecimal amount);
}

