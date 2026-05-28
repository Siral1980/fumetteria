package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


//La nostra bellissima interfaccia repository che ci permette di eseguire semplicissimi query senza
//scrivere una beata ceppa di codice SQL.


@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {

    boolean existsByTitle(String title);
    Optional<Comic> findByTitle(String title);
    Optional<List<Comic>>findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
    Optional<List<Comic>> findByOutOfStock(Boolean outOfStock);
}
