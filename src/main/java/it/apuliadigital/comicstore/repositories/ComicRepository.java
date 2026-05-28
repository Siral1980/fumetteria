package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    boolean existsByTitleIgnoreCase(String title);

    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String title, String author, String genre
    );

    List<Comic> findByPriceOrQuantity(Double price, int quantity);
    Optional<Comic> findByTitle(String title);
    Optional<Comic> findByTitleIgnoreCase(String title);
    Optional<Comic> findById(Long id);
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author
    );
    @Query("SELECT c.title FROM Comic c WHERE c.outOfStock = true")
    List<String> findTitlesByOutOfStockTrue();
}