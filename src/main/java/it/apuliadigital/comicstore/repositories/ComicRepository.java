package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic; // Controlla che il pacchetto sia corretto
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {
    // Per adesso la lasciamo vuota, ci servirà per i prossimi passi!
    Optional<Comic> findByTitle(String title);
}