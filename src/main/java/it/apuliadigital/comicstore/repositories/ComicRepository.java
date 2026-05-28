package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    
    Optional<Comic> findByTitle(String title);

    // Task 8: Cerca tramite stringa parziale case-insensitive su autore o titolo
    List<Comic> findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(String author, String title);

    // Task 10: Ritorna solo ed esclusivamente la lista dei nomi dei fumetti out of stock
    @Query("SELECT c.title FROM Comic c WHERE c.outOfStock = true")
    List<String> findTitlesOfOutOfStockComics();
}