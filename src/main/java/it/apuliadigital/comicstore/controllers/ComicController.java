package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    @PostMapping
    public ResponseEntity<Comic> createComic(@RequestBody Comic comic) {
        return new ResponseEntity<>(comicService.addComic(comic), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<?> getComicByTitle(@RequestParam String title) {
        try {
            Comic comic = comicService.findComicByTitle(title);
            return ResponseEntity.ok(comic);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<?> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Comic updatedComic = comicService.stockComic(id, quantity);
            return ResponseEntity.ok(updatedComic);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/sell")
    public ResponseEntity<?> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Comic updatedComic = comicService.sellComic(id, quantity);
            return ResponseEntity.ok(updatedComic);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
        try {
            Comic updatedComic = comicService.updateComic(id, comic);
            return ResponseEntity.ok(updatedComic);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}