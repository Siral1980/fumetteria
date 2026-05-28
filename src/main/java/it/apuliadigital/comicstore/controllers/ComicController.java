package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    // ========================================================================
    // TASK 3: Add Comic
    // ========================================================================
    @PostMapping
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic savedComic = comicService.addComic(comic);
        return new ResponseEntity<>(savedComic, HttpStatus.CREATED);
    }

    // ========================================================================
    // TASK 4: Find Comic (Ricerca per Titolo)
    // ========================================================================
    @GetMapping("/{title}")
    public ResponseEntity<Comic> findByTitle(@PathVariable String title) {
        Comic comic = comicService.findByTitle(title);
        return ResponseEntity.ok(comic);
    }

    // ========================================================================
    // TASK 5: Stock Comic (Aggiunta in magazzino)
    // ========================================================================
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(comic);
    }

    // ========================================================================
    // TASK 6: Sell Comic (Vendita)
    // ========================================================================
    @PatchMapping("/{id}/sell")
    public ResponseEntity<Comic> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.sellComic(id, quantity);
        return ResponseEntity.ok(comic);
    }

    // ========================================================================
    // TASK 7: Update Comic
    // ========================================================================
    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
        Comic updatedComic = comicService.updateComic(id, comic);
        return ResponseEntity.ok(updatedComic);
    }

    // ========================================================================
    // TASK 8: Find By Filter (Ricerca parziale autore e titolo)
    // ========================================================================
    @GetMapping("/search")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String q) {
        List<Comic> comics = comicService.findByFilter(q);
        return ResponseEntity.ok(comics);
    }

    // ========================================================================
    // TASK 9: Out of Stock Toggle
    // ========================================================================
    @PatchMapping("/toggle-stock")
    public ResponseEntity<String> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return ResponseEntity.ok("Toggle outOfStock eseguito con successo su tutto il database");
    }

    // ========================================================================
    // TASK 10: Find Low Stock (Restituisce solo i titoli)
    // ========================================================================
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<String>> findLowStock() {
        List<String> titles = comicService.findLowStock();
        return ResponseEntity.ok(titles);
    }
}
