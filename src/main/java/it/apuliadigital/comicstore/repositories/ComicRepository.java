package it.apuliadigital.comicstore.repositories;

import it.apuliadigital.comicstore.models.Comic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long> {

    /**
     * Verifica se esiste un fumetto con quel titolo, ignorando maiuscole/minuscole.
     *
     * @param title titolo del fumetto
     * @return true se esiste almeno un fumetto con quel titolo
     */
    boolean existsByTitleIgnoreCase(String title);

    /**
     * Cerca fumetti per titolo, autore o genere, anche parzialmente e senza distinzione tra maiuscole e minuscole.
     *
     * @param title titolo da cercare
     * @param author autore da cercare
     * @param genre genere da cercare
     * @return elenco di fumetti che corrispondono a uno dei criteri
     */
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String title, String author, String genre
    );

    /**
     * Trova fumetti con un prezzo specifico o una quantità specifica.
     *
     * @param price prezzo del fumetto
     * @param quantity quantità disponibile
     * @return elenco di fumetti con prezzo o quantità corrispondenti
     */
    List<Comic> findByPriceOrQuantity(Double price, int quantity);

    /**
     * Cerca un fumetto esatto per titolo.
     *
     * @param title titolo del fumetto
     * @return fumetto trovato, se presente
     */
    Optional<Comic> findByTitle(String title);

    /**
     * Cerca un fumetto per titolo senza distinzione tra maiuscole e minuscole.
     *
     * @param title titolo del fumetto
     * @return fumetto trovato, se presente
     */
    Optional<Comic> findByTitleIgnoreCase(String title);

    /**
     * Cerca un fumetto per id.
     *
     * @param id identificatore del fumetto
     * @return fumetto trovato, se presente
     */
    Optional<Comic> findById(Long id);

    /**
     * Cerca fumetti per titolo o autore, anche parzialmente e senza distinzione tra maiuscole e minuscole.
     *
     * @param title titolo da cercare
     * @param author autore da cercare
     * @return elenco di fumetti che corrispondono al titolo o autore
     */
    List<Comic> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(
            String title, String author
    );

    /**
     * Restituisce i titoli dei fumetti che sono esauriti.
     *
     * @return elenco di titoli di fumetti non disponibili
     */
    @Query("SELECT c.title FROM Comic c WHERE c.outOfStock = true")
    List<String> findTitlesByOutOfStockTrue();
}