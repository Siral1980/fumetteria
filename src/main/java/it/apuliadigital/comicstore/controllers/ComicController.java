package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Comic> getComicByTitle(@RequestParam String title) {
        return ResponseEntity.ok(comicService.findComicByTitle(title));
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.stockComic(id, quantity));
    }

    @PutMapping("/{id}/sell")
    public ResponseEntity<Comic> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.sellComic(id, quantity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
        return ResponseEntity.ok(comicService.updateComic(id, comic));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Comic>> searchComics(@RequestParam String keyword) {
        return ResponseEntity.ok(comicService.searchByKeyword(keyword));
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<String>> getOutOfStockTitles() {
        return ResponseEntity.ok(comicService.getOutOfStockTitles());
    }

    // Endpoint Bonus per vedere lo storico delle vendite
    @GetMapping("/sells")
    public ResponseEntity<List<Sell>> getAllSells() {
        return ResponseEntity.ok(comicService.getAllSells());
    }
}