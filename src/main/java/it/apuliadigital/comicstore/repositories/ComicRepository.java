package it.apuliadigital.comicstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.apuliadigital.comicstore.models.Comic;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    Optional<Comic> findByTitle(String title);

    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

    List<Comic> findByOutOfStockTrue();
}
