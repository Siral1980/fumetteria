package it.apuliadigital.comicstore.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.exceptions.ComicNotFoundException;
import it.apuliadigital.comicstore.exceptions.InsufficientStockException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;

/**
 * Service layer for managing comics and sales operations.
 * Handles business logic for inventory management and transactions.
 */
@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SellRepository sellRepository;

    /**
     * Add a new comic to inventory with 0 initial quantity.
     * @param comic the comic to add
     * @return the saved comic with ID
     */
    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        return comicRepository.save(comic);
    }

    /**
     * Retrieve all comics from inventory.
     * @return list of all comics
     */
    public List<Comic> getAllComics() {
        return comicRepository.findAll();
    }

    /**
     * Get a comic by ID.
     * @param id the comic ID
     * @return Optional containing the comic if found
     */
    public Optional<Comic> getComicById(Long id) {
        return comicRepository.findById(id);
    }

    /**
     * Find a comic by title.
     * @param title the title to search for
     * @return Optional containing the comic if found
     */
    public Optional<Comic> findByTitle(String title) {
        return comicRepository.findByTitle(title);
    }

    /**
     * Increase stock for a comic.
     * @param id the comic ID
     * @param quantity the amount to add
     * @return updated comic
     * @throws ComicNotFoundException if comic not found
     */
    public Comic stockComic(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        comic.setQuantity(comic.getQuantity() + quantity);
        return comicRepository.save(comic);
    }

    /**
     * Sell comic copies without recording transaction details.
     * @param id the comic ID
     * @param quantity the amount to sell
     * @return updated comic with reduced stock
     * @throws ComicNotFoundException if comic not found
     * @throws InsufficientStockException if not enough quantity available
     */
    public Comic sellComic(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        if (comic.getQuantity() < quantity) {
            throw new InsufficientStockException("Copie disponibili insufficienti");
        }
        comic.setQuantity(comic.getQuantity() - quantity);
        return comicRepository.save(comic);
    }

    /**
     * Sell comic copies and create a sale transaction record.
     * @param id the comic ID
     * @param quantity the amount to sell
     * @return created Sell transaction record
     * @throws ComicNotFoundException if comic not found
     * @throws InsufficientStockException if not enough quantity available
     */
    public Sell sellComicWithSell(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        if (comic.getQuantity() < quantity) {
            throw new InsufficientStockException("Copie disponibili insufficienti");
        }
        comic.setQuantity(comic.getQuantity() - quantity);
        comicRepository.save(comic);

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(BigDecimal.valueOf(comic.getPrice() * quantity));
        return sellRepository.save(sell);
    }

    /**
     * Update comic details.
     * @param id the comic ID
     * @param updatedComic the updated comic data
     * @return updated comic
     * @throws ComicNotFoundException if comic not found
     */
    public Comic updateComic(Long id, Comic updatedComic) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        comic.setTitle(updatedComic.getTitle());
        comic.setAuthor(updatedComic.getAuthor());
        comic.setPrice(updatedComic.getPrice());
        comic.setGenre(updatedComic.getGenre());
        return comicRepository.save(comic);
    }

    /**
     * Find sales within a date range.
     * @param start start date/time
     * @param end end date/time
     * @return list of sales in range
     */
    public List<Sell> findSellsByDateRange(LocalDateTime start, LocalDateTime end) {
        return sellRepository.findBySellingDateBetween(start, end);
    }

    /**
     * Find sales above a certain amount.
     * @param amount minimum amount threshold
     * @return list of sales matching criteria
     */
    public List<Sell> findSellsByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}