package it.apuliadigital.comicstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    // Cerca un fumetto in base al titolo esatto.
    Optional<Comic> findByTitle(String title);

    // Cerca i fumetti che contengono una determinata parola nel titolo OPPURE nel nome dell'autore.
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    // Recupera la lista di tutti i fumetti che sono attualmente fuori magazzino.
    List<Comic> findByOutOfStockTrue();
}
