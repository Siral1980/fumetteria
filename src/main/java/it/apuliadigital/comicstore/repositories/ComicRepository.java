package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Integer> {

    boolean existsByTitle(String title);
    Optional<Comic> findByTitle(String title);
}
