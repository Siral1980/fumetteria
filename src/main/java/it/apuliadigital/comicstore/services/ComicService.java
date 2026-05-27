package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.exceptions.InsufficientStockException;
import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Log4j2
@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

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

    public Comic findComicByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Comic with title " + title + " not found"));
    }

    public Comic addStock(String title, int quantityToAdd) {
        Comic comic = findComicByTitle(title);
        comic.setQuantity(comic.getQuantity() + quantityToAdd);

        log.info("Warehouse update for {}: New quantity {}", title, comic.getQuantity());
        return comicRepository.save(comic);
    }

    public Comic sellComic(String title, int quantityToSell) {
        Comic comic = findComicByTitle(title);

        if (comic.getQuantity() < quantityToSell) {
            throw new InsufficientStockException(
                    "Insufficient stock for '" + title + "'. Available: " + comic.getQuantity()
            );
        }
        comic.setQuantity(comic.getQuantity() - quantityToSell);

        log.info("Sale completed for '{}': Remaining stock {}", title, comic.getQuantity());

        return comicRepository.save(comic);
    }


    }
