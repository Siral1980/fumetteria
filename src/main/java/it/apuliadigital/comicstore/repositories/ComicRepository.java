package it.apuliadigital.comicstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    boolean existsByTitleAndAuthorAndGenreAndPrice(String title, String author, String genre, double price);

    Optional<Comic> findByTitleIgnoreCase(String title);

    // Task 8 - ricerca parziale case-insensitive su titolo o autore
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    // Task 10 - tutti i fumetti esauriti
    List<Comic> findByOutOfStockTrue();
}