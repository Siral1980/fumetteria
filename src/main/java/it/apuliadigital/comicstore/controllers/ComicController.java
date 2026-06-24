package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    private final ComicService comicService;

    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    // ── POST /api/comics ──────────────────────────────────────────────────────

    /**
     * Crea un nuovo fumetto.
     * quantity viene forzata a 0 dal service.
     */
    @PostMapping
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic saved = comicService.addComic(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ── GET /api/comics/search?title=... ──────────────────────────────────────

    /**
     * Ricerca un fumetto per titolo (univoco).
     */
    @GetMapping("/search")
    public ResponseEntity<Comic> findByTitle(@RequestParam String title) {
        Comic comic = comicService.findByTitle(title);
        return ResponseEntity.ok(comic);
    }

    // ── GET /api/comics/filter?query=... ──────────────────────────────────────

    /**
     * Cerca fumetti per titolo o autore (match parziale).
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String query) {
        return ResponseEntity.ok(comicService.findByFilter(query));
    }

    // ── PATCH /api/comics/{id}/sell?quantity=... ──────────────────────────────

    /**
     * Vende un certo numero di copie del fumetto.
     * Scala la quantity del Comic e salva la Sell con totalAmount calcolato.
     */
    @PatchMapping("/{id}/sell")
    public ResponseEntity<Sell> sellComic(
            @PathVariable Long id,
            @RequestParam int quantity) {
        Sell sell = comicService.sellComic(id, quantity);
        return ResponseEntity.ok(sell);
    }

    // ── PATCH /api/comics/{id}/stock?quantity=... ─────────────────────────────

    /**
     * Aggiunge stock al fumetto e aggiorna lo stato outOfStock.
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(
            @PathVariable Long id,
            @RequestParam int quantity) {
        Comic updated = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(updated);
    }

    // ── PATCH /api/comics/{id}/quantity?quantity=... ──────────────────────────

    /**
     * Aggiorna la quantità disponibile (stock) di un fumetto.
     */
    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Comic> updateQuantity(
            @PathVariable Long id,
            @RequestParam int quantity) {
        Comic updated = comicService.updateQuantity(id, quantity);
        return ResponseEntity.ok(updated);
    }

    // ── PUT /api/comics/{id} ──────────────────────────────────────────────────

    /**
     * Aggiorna i dati anagrafici del fumetto.
     * id e quantity non sono modificabili tramite questo endpoint.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(
            @PathVariable Long id,
            @RequestBody Comic comic) {
        Comic updated = comicService.updateComic(id, comic);
        return ResponseEntity.ok(updated);
    }

    // ── GET /api/comics/toggle-out-of-stock ───────────────────────────────────

    /**
     * Cicla il database e aggiorna il flag outOfStock in base alla quantità.
     */
    @GetMapping("/toggle-out-of-stock")
    public ResponseEntity<List<Comic>> toggleOutOfStock() {
        return ResponseEntity.ok(comicService.toggleOutOfStock());
    }

    // ── GET /api/comics/out-of-stock ─────────────────────────────────────────

    /**
     * Restituisce solo i titoli dei fumetti out of stock.
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<String>> findLowStockTitles() {
        return ResponseEntity.ok(comicService.findLowStockTitles());
    }
}