package it.apuliadigital.comicstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Comic;

@Repository
public interface ComicRepository  extends JpaRepository<Comic, Integer> {

    boolean existsByTitleAndAuthorAndGenreAndPriceAllIgnoreCase(
        String title, String author, String genre, double price
    );
    Optional<Comic> findByTitleAllIgnoreCase(String title);
    
    List<Comic> findByTitleContainingAllIgnoreCase(String title);
    List<Comic> findByAuthorContainingAllIgnoreCase(String author);
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingAllIgnoreCase(
        String titleKeyword, String authorKeyword
    );
}
