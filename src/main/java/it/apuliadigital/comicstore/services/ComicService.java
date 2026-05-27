package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    // ── Add Comic ─────────────────────────────────────────────────────────────

    /**
     * Aggiunge un nuovo fumetto con quantity = 0 (default).
     * Il campo quantity viene sempre forzato a 0 indipendentemente dal body.
     */
    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        return comicRepository.save(comic);
    }

    // ── Find Comic ────────────────────────────────────────────────────────────

    /**
     * Cerca un fumetto tramite il titolo (univoco per @Column unique).
     * Lancia EntityNotFoundException se non trovato.
     */
    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
            .orElseThrow(() -> new EntityNotFoundException(
                "Comic not found with title: " + title
            ));
    }

    // ── Sell Comic ────────────────────────────────────────────────────────────

    /**
     * Vende una certa quantità di copie del fumetto.
     * Salva la Sell tramite cascade su Comic (nessun SellRepository necessario).
     */
    public Sell sellComic(Long comicId, int sellingQuantity) {
        if (sellingQuantity <= 0) {
            throw new IllegalArgumentException("Selling quantity must be greater than 0");
        }

        Comic comic = comicRepository.findById(comicId)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + comicId));

        if (comic.getQuantity() < sellingQuantity) {
            throw new IllegalStateException(
                "Not enough copies available. Requested: " + sellingQuantity +
                ", Available: " + comic.getQuantity()
            );
        }

        // Crea la vendita
        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(sellingQuantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(comic.getPrice().multiply(BigDecimal.valueOf(sellingQuantity)));

        // Scala le copie e aggiunge la sell → salvata in cascata con comicRepository.save()
        comic.setQuantity(comic.getQuantity() - sellingQuantity);
        comic.getSells().add(sell);
        comicRepository.save(comic);

        return sell;
    }

    // ── Update Quantity (Stock Comic) ─────────────────────────────────────────

    /**
     * Aggiorna la quantità disponibile di un fumetto esistente.
     * Metodo separato da updateComic: è l'unico autorizzato a modificare quantity.
     */
    public Comic updateQuantity(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Comic comic = comicRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + id));

        comic.setQuantity(quantity);
        return comicRepository.save(comic);
    }

    // ── Update Comic ──────────────────────────────────────────────────────────

    /**
     * Aggiorna i dati anagrafici del fumetto.
     * id e quantity non vengono mai modificati da questo metodo.
     */
    public Comic updateComic(Long id, Comic updatedComic) {
        Comic comic = comicRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + id));

        comic.setTitle(updatedComic.getTitle());
        comic.setAuthor(updatedComic.getAuthor());
        comic.setPublisher(updatedComic.getPublisher());
        comic.setYear(updatedComic.getYear());
        comic.setPrice(updatedComic.getPrice());

        return comicRepository.save(comic);
    }

    public Comic addComic(String title, String author, String genre, Double price) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addComic'");
    }
}