package it.apuliadigital.comicstore.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    /**
     * Adds a new comic with quantity = 0
     */
    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    /**
     * Finds a comic by title
     */
    public Optional<Comic> findComicByTitle(String title) {
        return comicRepository.findByTitle(title);
    }

    /**
     * Adds quantity to comic stock
     */
    public Comic stockComic(Long comicId, int quantity) {
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found with id: " + comicId));
        comic.setQuantity(comic.getQuantity() + quantity);
        return comicRepository.save(comic);
    }

    /**
     * Sells a comic (checks availability and decreases quantity)
     */
    public Comic sellComic(Long comicId, int quantity) {
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found with id: " + comicId));
        
        if (comic.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + comic.getQuantity());
        }
        
        comic.setQuantity(comic.getQuantity() - quantity);
        return comicRepository.save(comic);
    }

    /**
     * Updates a comic (without changing id or quantity)
     */
    public Comic updateComic(Long comicId, Comic comicDetails) {
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found with id: " + comicId));
        
        if (comicDetails.getTitle() != null && !comicDetails.getTitle().isEmpty()) {
            comic.setTitle(comicDetails.getTitle());
        }
        if (comicDetails.getAuthor() != null) {
            comic.setAuthor(comicDetails.getAuthor());
        }
        if (comicDetails.getPrice() != null) {
            comic.setPrice(comicDetails.getPrice());
        }
        if (comicDetails.getGenre() != null) {
            comic.setGenre(comicDetails.getGenre());
        }
        
        return comicRepository.save(comic);
    }

    /**
     * Gets a comic by id
     */
    public Optional<Comic> getComicById(Long id) {
        return comicRepository.findById(id);
    }

    /**
     * Finds comics by partial filter on title or author
     */
    public List<Comic> findByFilter(String filter) {
        return comicRepository.findByFilter(filter);
    }

    /**
     * Toggles the outOfStock status for all comics based on quantity
     */
    public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            comic.setOutOfStock(comic.getQuantity() <= 0);
            comicRepository.save(comic);
        }
    }

    /**
     * Finds all out of stock comic names
     */
    public List<String> findLowStock() {
        return comicRepository.findOutOfStockNames();
    }
}

