package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    // Genera la query automaticamente dal nome del metodo
    Optional<Comic> findByTitle(String title);
}
