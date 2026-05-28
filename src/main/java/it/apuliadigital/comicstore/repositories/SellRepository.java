package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Sell;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


//La nostra bellissima interfaccia repository che ci permette di eseguire semplicissimi query senza
//scrivere una beata ceppa di codice SQL.


@Repository
public interface SellRepository extends JpaRepository<Sell, Integer> {
    Optional<List<Sell>> findBySellingDateBetween(LocalDate start, LocalDate end);
}
