package it.apuliadigital.comicstore.repositories; // Pacchetto dei repository.
 
import java.util.Optional; // Optional serve quando una ricerca potrebbe non trovare nulla.
 
import org.springframework.data.jpa.repository.JpaRepository; // Repository gia pronto con save, findById, findAll, delete, ecc.
import org.springframework.stereotype.Repository; // Dice a Spring che questa interfaccia e un repository.
 
import it.apuliadigital.comicstore.models.Comic; // Importiamo il model Comic.
 
@Repository // Spring deve gestire questa interfaccia come componente repository.
public interface ComicRepository extends JpaRepository<Comic, Long>  { // Repository per Comic con id di tipo Long.
 
     Optional<Comic> findByTitle(String title); // Cerca un fumetto tramite titolo.
}