package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
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
}



