package it.apuliadigital.comicstore.repository;

import it.apuliadigital.comicstore.model.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// estendo JpaRepository così ho già i metodi base tipo save, findById, findAll ecc
// non devo scrivere le query semplici a mano
@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    // spring data capisce da solo la query dal nome del metodo, molto comodo
    // IgnoreCase così funziona anche se scrivo "one piece" invece di "One Piece"
    Optional<Comic> findByTitleIgnoreCase(String title);

    // qui ho dovuto scrivere la query a mano con @Query perché
    // la ricerca "contiene" su due campi diversi non si fa solo col nome del metodo
    // LOWER serve per rendere la ricerca case-insensitive
    @Query("SELECT c FROM Comic c WHERE " +
           "LOWER(c.title)  LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.author) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Comic> findByTitleOrAuthorContaining(@Param("query") String query);

    // cerca tutti i fumetti con outOfStock = true
    // anche qui spring genera la query dal nome
    List<Comic> findByOutOfStockTrue();
}
