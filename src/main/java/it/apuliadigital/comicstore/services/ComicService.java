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

    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Error: comic " + title + " not found"));
    }

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

    public List<Comic> findByFilter(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return comicRepository.findAll();
        }
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrGenreContainingIgnoreCase(
                keyword, keyword, keyword
        );
    }

    public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            comic.setOutOfStock(comic.getQuantity() <= 0);
        }
        comicRepository.saveAll(allComics);
    }

    public List<String> findLowStockTitles() {
        return comicRepository.findTitlesByOutOfStockTrue();
    }
}