package it.apuliadigital.comicstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {

    boolean existsByTitleAndAuthorAndGenreAndPriceAfterAllIgnoreCase(String title, String author, String genre, double price, int quantity);

    Optional<Comic> findByTitleAllIgnoreCase(String title);

    
}