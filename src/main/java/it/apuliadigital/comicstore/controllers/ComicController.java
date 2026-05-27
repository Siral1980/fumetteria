package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for managing comic books and sales operations.
 * Provides endpoints for CRUD operations on comics and sales tracking.
 */
@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    /**
     * Add a new comic to the store inventory.
     * @param comic the comic to add
     * @return created comic with assigned ID
     */
    @PostMapping
    public ResponseEntity<Comic> addComic(@Valid @RequestBody Comic comic) {
        Comic saved = comicService.addComic(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * Retrieve all comics from inventory.
     * @return list of all comics
     */
    @GetMapping
    public ResponseEntity<List<Comic>> getAllComics() {
        List<Comic> comics = comicService.getAllComics();
        return ResponseEntity.ok(comics);
    }

    /**
     * Get a comic by its ID.
     * @param id the comic ID
     * @return the comic or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Comic> getComicById(@PathVariable Long id) {
        Optional<Comic> comic = comicService.getComicById(id);
        return comic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Find comic by title (case-insensitive search).
     * @param title the title to search for
     * @return the comic or 404 if not found
     */
    @GetMapping("/search")
    public ResponseEntity<Comic> findByTitle(@RequestParam String title) {
        Optional<Comic> comic = comicService.findByTitle(title);
        return comic.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add stock quantity to a comic.
     * @param id the comic ID
     * @param quantity the quantity to add
     * @return updated comic
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(comic);
    }

    /**
     * Sell comic(s) without recording the transaction.
     * @param id the comic ID
     * @param quantity the quantity to sell
     * @return updated comic with reduced stock
     */
    @PatchMapping("/{id}/sell")
    public ResponseEntity<Comic> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.sellComic(id, quantity);
        return ResponseEntity.ok(comic);
    }

    /**
     * Sell comic(s) and record the transaction.
     * @param id the comic ID
     * @param quantity the quantity to sell
     * @return created sale record
     */
    @PostMapping("/{id}/sellWithRecord")
    public ResponseEntity<Sell> sellComicWithSell(@PathVariable Long id, @RequestParam int quantity) {
        Sell sell = comicService.sellComicWithSell(id, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sell);
    }

    /**
     * Update comic details.
     * @param id the comic ID
     * @param comic the updated comic data
     * @return updated comic
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @Valid @RequestBody Comic comic) {
        Comic updated = comicService.updateComic(id, comic);
        return ResponseEntity.ok(updated);
    }

    /**
     * Find sales within a date range.
     * @param start start date/time (ISO format)
     * @param end end date/time (ISO format)
     * @return list of sales in range
     */
    @GetMapping("/sells/date-range")
    public ResponseEntity<List<Sell>> findSellsByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<Sell> sells = comicService.findSellsByDateRange(start, end);
        return ResponseEntity.ok(sells);
    }

    /**
     * Find sales above a certain amount.
     * @param amount minimum amount threshold
     * @return list of sales matching criteria
     */
    @GetMapping("/sells/amount")
    public ResponseEntity<List<Sell>> findSellsByAmount(@RequestParam BigDecimal amount) {
        List<Sell> sells = comicService.findSellsByAmountGreaterThan(amount);
        return ResponseEntity.ok(sells);
    }
}