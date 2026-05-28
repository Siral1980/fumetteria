package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SellRepository extends JpaRepository<Sell, Long> {

    /**
     * Restituisce le vendite avvenute in un intervallo di date.
     *
     * @param start data/ora di inizio
     * @param end data/ora di fine
     * @return elenco di vendite tra le due date
     */
    List<Sell> findBySellingDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Restituisce le vendite con importo totale maggiore di un valore.
     *
     * @param amount importo minimo
     * @return elenco di vendite con totale superiore all'importo
     */
    List<Sell> findByTotalAmountGreaterThan(BigDecimal amount);
}