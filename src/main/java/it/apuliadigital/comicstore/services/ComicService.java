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
    // TASK 3: Add Comic
    // N.B. Il fumetto deve essere creato con 0 di default nel campo quantity
    // ========================================================================
    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    // ========================================================================
    // TASK 4: Find Comic
    // N.B. Ricerca tramite il titolo
    // ========================================================================
    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con titolo: " + title));
    }

    // ========================================================================
    // TASK 5: Stock Comic
    // Aggiunta in magazzino del fumetto
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
    // TASK 6: Sell Comic
    // Vendita del fumetto controllando prima la disponibilità
    // ========================================================================
    public Comic sellComic(Long id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità da vendere deve essere maggiore di 0");
        }
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + id));
        if (comic.getQuantity() < quantity) {
            throw new IllegalArgumentException("Copie disponibili insufficienti. Disponibili: " + comic.getQuantity());
        }
        comic.setQuantity(comic.getQuantity() - quantity);
        return comicRepository.save(comic);
    }

    // ========================================================================
    // TASK 7: Update Comic
    // Aggiornamento senza cambiare id o quantità
    // ========================================================================
    public Comic updateComic(Long id, Comic updatedComic) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + id));
        comic.setTitle(updatedComic.getTitle());
        comic.setAuthor(updatedComic.getAuthor());
        comic.setPrice(updatedComic.getPrice());
        comic.setGenre(updatedComic.getGenre());
        return comicRepository.save(comic);
    }

    // ========================================================================
    // TASK 8: Find By Filter
    // Ricerca tramite stringa parziale su autore e titolo
    // ========================================================================
    public List<Comic> findByFilter(String filter) {
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(filter, filter);
    }

    // ========================================================================
    // TASK 9: Out of Stock Toggle
    // Cicla tutto il database e fa "toggle" sul campo outOfStock
    // ========================================================================
    public void toggleOutOfStock() {
        List<Comic> comics = comicRepository.findAll();
        for (Comic comic : comics) {
            comic.setOutOfStock(comic.getQuantity() <= 0);
        }
        comicRepository.saveAll(comics);
    }

    // ========================================================================
    // TASK 10: Find Low Stock
    // Ricerca tutti i fumetti "out of stock", ritorna solo i titoli
    // ========================================================================
    public List<String> findLowStock() {
        List<Comic> comics = comicRepository.findByOutOfStockTrue();
        return comics.stream()
                .map(Comic::getTitle)
                .toList();
    }
}
