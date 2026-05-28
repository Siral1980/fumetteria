package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    // ========================================================================
    // Add Comic
    // ========================================================================
    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    // ========================================================================
    // Find Comic
    // ========================================================================
    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con titolo: " + title));
    }

    // ========================================================================
    // Stock Comic
    // ========================================================================
    public Comic stockComic(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità da aggiungere deve essere maggiore di 0");
        }
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + id));
        comic.setQuantity(comic.getQuantity() + quantity);
        return comicRepository.save(comic);
    }



    // ========================================================================
    // Update Comic
    // ========================================================================
    public Comic updateComic(Long id, Comic updatedComic) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + id));

        if (updatedComic.getTitle() != null && !updatedComic.getTitle().isEmpty() && !updatedComic.getTitle().equals("string")) {
            comic.setTitle(updatedComic.getTitle());
        }
        if (updatedComic.getAuthor() != null && !updatedComic.getAuthor().isEmpty() && !updatedComic.getAuthor().equals("string")) {
            comic.setAuthor(updatedComic.getAuthor());
        }
        if (updatedComic.getGenre() != null && !updatedComic.getGenre().isEmpty() && !updatedComic.getGenre().equals("string")) {
            comic.setGenre(updatedComic.getGenre());
        }
        if (updatedComic.getPrice() != null && updatedComic.getPrice() != 0 && updatedComic.getPrice() != 0.1) {
            comic.setPrice(updatedComic.getPrice());
        }

        return comicRepository.save(comic);
    }

    // ========================================================================
    // Find By Filter
    // ========================================================================
    public List<Comic> findByFilter(String filter) {
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(filter, filter);
    }

    // ========================================================================
    // Out of Stock Toggle
    // ========================================================================
    public List<Comic> toggleOutOfStock() {
        List<Comic> comics = comicRepository.findAll();
        for (Comic comic : comics) {
            comic.setOutOfStock(comic.getQuantity() <= 0);
        }
        return comicRepository.saveAll(comics);
    }

    // ========================================================================
    // Find Low Stock
    // ========================================================================
    public List<String> findLowStock() {
        List<Comic> comics = comicRepository.findByOutOfStockTrue();
        return comics.stream()
                .map(Comic::getTitle)
                .toList();
    }
}
