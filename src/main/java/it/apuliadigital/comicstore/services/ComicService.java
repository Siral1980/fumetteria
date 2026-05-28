package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.exceptions.InsufficientStockException;
import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
@Log4j2
@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;


    //permette di aggiungere dei volumi ma in caso di fumetto già inserito viene visualizzata l'exception
    public Comic addComic(Comic comic, String requestId) {
        Instant start = Instant.now();
        log.info("ComicService - started at {}. [RequestID: {}]", start, requestId);

        try {
            boolean isFound = comicRepository.existsByTitleIgnoreCaseAndAuthorIgnoreCaseAndGenreAllIgnoreCase(
                    comic.getTitle(),
                    comic.getAuthor(),
                    comic.getGenre()
            );

            if (isFound) {
                throw new IllegalArgumentException("Error: comic " + comic.getTitle().toUpperCase() + " already exists");
            }

            comic.setQuantity(0);
            return comicRepository.save(comic);

        } catch (IllegalArgumentException e) {
            log.error("Duplication try blocked: {}, [RequestId]: {}", e.getMessage(), requestId);
            throw e;
        } finally {
            log.info("ComicService - execution finished for RequestID: {}", requestId);
        }
    }


    //cerca i fumetti per titolo
    public Comic findComicByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Comic with title " + title + " not found"));
    }

    //aggiunge al magazzino i volumi
    public Comic addStock(String title, int quantityToAdd) {
        Comic comic = findComicByTitle(title);
        comic.setQuantity(comic.getQuantity() + quantityToAdd);

        log.info("Warehouse update for {}: New quantity {}", title, comic.getQuantity());
        return comicRepository.save(comic);
    }



    //metodo per visualizzare la vendita, solo se i volumi sono disponibili altrimenti partirà l'exception
    public Comic sellComic(String title, int quantityToSell, Comic updatedDetails) {
        Comic comic = findComicByTitle(title);

        if (comic.getQuantity() < quantityToSell) {
            throw new InsufficientStockException(
                    "Insufficient stock for '" + title + "'. Available: " + comic.getQuantity()
            );
        }
        //permette di aggiornare titolo, autore e genere senza modificare id e quantità
        if (updatedDetails != null) {
            comic.setTitle(updatedDetails.getTitle());
            comic.setAuthor(updatedDetails.getAuthor());
            comic.setGenre(updatedDetails.getGenre());
        }
        comic.setQuantity(comic.getQuantity() - quantityToSell);
        comic.setOutOfStock(comic.getQuantity() <= 0);



        log.info("Sale completed for '{}': Remaining stock {}", title, comic.getQuantity());

        return comicRepository.save(comic);
    }


    //cerca il volume o i volumi cercati
    public List<Comic> findComicsByFilter(String query) {
        if (query == null || query.isBlank()) {
            //visualizza tutti i volumi se la richiesta è vuota
            log.info("Empty filter: returning all comics");
            return comicRepository.findAll();
        }

        String sanitizedQuery = query.trim();
        Set<Comic> combinedResults = new LinkedHashSet<>();

        List<Comic> textResults = comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                sanitizedQuery, sanitizedQuery, sanitizedQuery
        );
        combinedResults.addAll(textResults);

        log.info("Filter '{}' returned {} results", sanitizedQuery, combinedResults.size());

        return new ArrayList<>(combinedResults);
    }


    //disattiva tutti i volumi che hanno come quantità 0
    public String toggleAllOutOfStock(String requestId) {
        log.info("ComicService - Global toggle started. [RequestID: {}]", requestId);

        List<Comic> allComics = comicRepository.findAll();

        if (allComics.isEmpty()) {
            return "No comics found in database to toggle.";
        }

        for (Comic comic : allComics) {
            boolean isNowOutOfStock = (comic.getQuantity() <= 0);
            comic.setOutOfStock(isNowOutOfStock);
        }

        comicRepository.saveAll(allComics);

        log.info("ComicService - Global toggle finished. [RequestID: {}]", requestId);

        return "Toggle operation completed: " + allComics.size() + " comics updated based on their quantity.";
    }

//cerca i fumetti che stanno per terminare
    public List<String> findLowStockTitles() {
        log.info("Searching for all out-of-stock comics titles");

        List<Comic> outOfStockComics = comicRepository.findByOutOfStockTrue();
        return outOfStockComics.stream()
                .map(Comic::getTitle)
                .toList();
    }

  ;;  }
