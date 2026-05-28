package it.apuliadigital.comicstore.repository;

import it.apuliadigital.comicstore.model.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// repository per le vendite, stessa cosa di ComicRepository
@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {

    // Between cerca tra due date, spring capisce da solo
    // uso LocalDateTime così ho anche l'orario e non solo la data
    List<Sell> findBySoldAtBetween(LocalDateTime from, LocalDateTime to);

    // GreaterThan cerca tutti i record con totalPrice > amount
    List<Sell> findByTotalPriceGreaterThan(BigDecimal amount);
}
