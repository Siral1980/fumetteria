package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    // Aggiunge un nuovo fumetto
    @PostMapping
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic saved = comicService.addComic(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Cerca per titolo
    @GetMapping("/search")
    public ResponseEntity<Comic> findByTitle(@RequestParam String title) {
        return comicService.findByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
    try {
        Comic updated = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
 
    }
}

    // Vende copie del fumetto
    @PostMapping("/{id}/sell")
    public ResponseEntity<?> sellComic(@PathVariable Long id, @RequestParam int quantity) {
    try {
        Sell sell = comicService.sellComic(id, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sell);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    }

    // Aggiorna i dati del fumetto (no id, no quantity)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
    try {
        Comic updated = comicService.updateComic(id, comic);
        return ResponseEntity.ok(updated);
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}

    // Ricerca parziale su titolo e autore
    @GetMapping("/filter")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String query) {
        List<Comic> results = comicService.findByFilter(query);
        return ResponseEntity.ok(results);
}

    // Aggiorna outOfStock su tutti i fumetti
    @PatchMapping("/toggle-out-of-stock")
    public ResponseEntity<Void> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return ResponseEntity.noContent().build();
}

// Lista dei titoli esauriti

@GetMapping("/out-of-stock")
public ResponseEntity<List<String>> findOutOfStock() {
    List<String> results = comicService.findOutOfStock();
    return ResponseEntity.ok(results);
}
}



