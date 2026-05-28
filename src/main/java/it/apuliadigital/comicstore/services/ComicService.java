package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Log4j2
@Service
public class ComicService {
    @Autowired
    private ComicRepository comicRepository;

    /**
     * Aggiunge un nuovo fumetto al database.
     * Imposta la quantità a 0 e lo segna come esaurito.
     *
     * @param comic oggetto fumetto da salvare
     * @param requestId id della richiesta per il log
     * @return fumetto salvato
     */
    public Comic addComic(Comic comic, String requestId) {
        Instant start = Instant.now();
        log.info("ComicService - started at {}. [RequestID: {}", start, requestId);

        comic.setId(null);

        try {
            boolean isFound = comicRepository.existsByTitleIgnoreCase(comic.getTitle());

            if (isFound) {
                throw new IllegalArgumentException("Error: comic " + comic.getTitle().toUpperCase() + " already exists");
            }

            comic.setQuantity(0);
            comic.setOutOfStock(true);

        } catch (IllegalArgumentException e) {
            log.error("Duplication try blocked: {}, [RequestId]: {}", e.getMessage(), requestId);
            throw e;
        }

        Comic savedComic = comicRepository.save(comic);

        Instant end = Instant.now();
        long executionTime = Duration.between(start, end).toMillis();
        log.info("ComicService - finished in {} milliseconds. [RequestID: {}", executionTime, requestId);

        return savedComic;
    }

    /**
     * Cerca un fumetto con il titolo esatto.
     *
     * @param title titolo del fumetto
     * @return fumetto trovato
     */
    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Error: comic " + title + " not found"));
    }

    /**
     * Aggiunge copie a un fumetto esistente e aggiorna lo stato di disponibilità.
     *
     * @param id identificatore del fumetto
     * @param quantityToIncreate copie da aggiungere
     * @return fumetto aggiornato
     */
    public Comic stockComic(Long id, int quantityToIncreate) {
        if (quantityToIncreate <= 0) {
            throw new IllegalArgumentException("Error: quantity to add must be greater than 0");
        }

        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error: comic with ID " + id + " not found"));

        comic.setQuantity(comic.getQuantity() + quantityToIncreate);

        if (comic.getQuantity() > 0) {
            comic.setOutOfStock(false);
        }

        return comicRepository.save(comic);
    }

    /**
     * Vende copie di un fumetto, riducendo la quantità.
     *
     * @param id identificatore del fumetto
     * @param quantityToSell numero di copie da vendere
     * @return fumetto aggiornato
     */
    public Comic sellComic(Long id, int quantityToSell) {
        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("Error: quantity to sell must be greater than 0");
        }

        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error: comic with ID " + id + " not found"));

        if (comic.getQuantity() < quantityToSell) {
            throw new IllegalArgumentException("Error: not enough copies available. Available: " + comic.getQuantity());
        }

        comic.setQuantity(comic.getQuantity() - quantityToSell);

        if (comic.getQuantity() <= 0) {
            comic.setOutOfStock(true);
        }

        return comicRepository.save(comic);
    }

    /**
     * Aggiorna i campi modificabili di un fumetto esistente.
     *
     * @param id identificatore del fumetto
     * @param newComicData dati aggiornati del fumetto
     * @return fumetto aggiornato
     */
    public Comic updateComic(Long id, Comic newComicData) {
        Comic existingComic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Error: comic with ID " + id + " not found"));

        if (newComicData.getTitle() != null) {
            existingComic.setTitle(newComicData.getTitle());
        }
        if (newComicData.getAuthor() != null) {
            existingComic.setAuthor(newComicData.getAuthor());
        }
        if (newComicData.getGenre() != null) {
            existingComic.setGenre(newComicData.getGenre());
        }
        if (newComicData.getPrice() != null) {
            existingComic.setPrice(newComicData.getPrice());
        }

        return comicRepository.save(existingComic);
    }

    /**
     * Cerca fumetti usando una parola chiave su titolo, autore o genere.
     * Se la parola chiave è vuota, restituisce tutti i fumetti.
     *
     * @param keyword testo da cercare
     * @return lista di fumetti corrispondenti
     */
    public List<Comic> findByFilter(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return comicRepository.findAll();
        }
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                keyword, keyword, keyword
        );
    }

    /**
     * Aggiorna lo stato outOfStock di tutti i fumetti in base alla quantità.
     */
    public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            comic.setOutOfStock(comic.getQuantity() <= 0);
        }
        comicRepository.saveAll(allComics);
    }

    /**
     * Restituisce i titoli dei fumetti esauriti.
     *
     * @return elenco di titoli non disponibili
     */
    public List<String> findLowStockTitles() {
        return comicRepository.findTitlesByOutOfStockTrue();
    }
}