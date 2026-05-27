package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Comic> addComic(@RequestParam String title,
                                        @RequestParam String author,
                                        @RequestParam String genre,
                                        @RequestParam Double price) {
        Comic saved = comicService.addComic(title, author, genre, price);
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
}