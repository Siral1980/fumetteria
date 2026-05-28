package it.apuliadigital.comicstore.repositories; // Pacchetto repository.
 
import java.math.BigDecimal; // Tipo per importi.
import java.time.LocalDateTime; // Tipo per date e orari.
import java.util.List; // Lista di risultati.
 
import org.springframework.data.jpa.repository.JpaRepository; // Metodi CRUD pronti.
import org.springframework.stereotype.Repository; // Annotazione repository.
 
import it.apuliadigital.comicstore.models.Sell; // Model Sell.
 
@Repository // Dice a Spring di creare il repository.
public interface SellRepository extends JpaRepository<Sell, Long> { // Repository per Sell con id Long.
 
    List<Sell> findBySellingDateBetween(LocalDateTime startDate, LocalDateTime endDate); // Cerca vendite tra due date.
 
    List<Sell> findByTotalAmountGreaterThan(BigDecimal amount); // Cerca vendite con totale maggiore di un importo.
}