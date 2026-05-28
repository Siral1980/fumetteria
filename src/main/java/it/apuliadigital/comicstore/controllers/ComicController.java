package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ComicController {

    @Autowired
    private ComicService comicService;

    // ========================================================================
    // Add Comic
    // ========================================================================
    @PostMapping("/addComic")
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic savedComic = comicService.addComic(comic);
        return new ResponseEntity<>(savedComic, HttpStatus.CREATED);
    }

    // ========================================================================
    // Find Comic (Search by title)
    // ========================================================================
    @GetMapping("/{title}")
    public ResponseEntity<Comic> findByTitle(@PathVariable String title) {
        Comic comic = comicService.findByTitle(title);
        return ResponseEntity.ok(comic);
    }

    // ========================================================================
    // Stock Comic (Add comic to stock)
    // ========================================================================
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(comic);
    }



    // ========================================================================
    // Update Comic
    // ========================================================================
    @PutMapping("/{id}/updateComic")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
        Comic updatedComic = comicService.updateComic(id, comic);
        return ResponseEntity.ok(updatedComic);
    }

    // ========================================================================
    // Find By Filter (Partial search by title or author)
    // ========================================================================
    @GetMapping("/search")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String q) {
        List<Comic> comics = comicService.findByFilter(q);
        return ResponseEntity.ok(comics);
    }

    // ========================================================================
    // Out of Stock Toggle
    // ========================================================================
    @PatchMapping("/toggle-stock")
    public ResponseEntity<List<Comic>> toggleOutOfStock() {
        List<Comic> updatedComics = comicService.toggleOutOfStock();
        return ResponseEntity.ok(updatedComics);
    }

    // ========================================================================
    // Find Low Stock (Only title returned)
    // ========================================================================
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<String>> findLowStock() {
        List<String> titles = comicService.findLowStock();
        return ResponseEntity.ok(titles);
    }
}
