package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    boolean existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndGenreAllIgnoreCase(
            String title,
            String author,
            String genre
    );
    Optional<Comic> findByTitle(String title);
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String title,
            String author,
            String genre
    );
    List<Comic> findByOutOfStockTrue();
}
