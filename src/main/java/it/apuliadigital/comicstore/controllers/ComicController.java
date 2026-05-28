package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
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
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        return new ResponseEntity<>(comicService.addComic(comic), HttpStatus.CREATED);
    }

    // CORRETTO: Pulito, elegante, stile RESTful con PathVariable e gestione delegata all'ExceptionHandler
    @GetMapping("/title/{title}")
    public ResponseEntity<Comic> findComicByTitle(@PathVariable String title) {
        return new ResponseEntity<>(comicService.findComicByTitle(title), HttpStatus.OK);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        return new ResponseEntity<>(comicService.stockComic(id, quantity), HttpStatus.OK);
    }

    @PutMapping("/{id}/sell")
    public ResponseEntity<Comic> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        return new ResponseEntity<>(comicService.sellComic(id, quantity), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comicDetails) {
        return new ResponseEntity<>(comicService.updateComic(id, comicDetails), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String keyword) {
        return new ResponseEntity<>(comicService.findByFilter(keyword), HttpStatus.OK);
    }

    @PutMapping("/toggle-out-of-stock")
    public ResponseEntity<Void> toggleOutOfStock() {
        comicService.toggleOutOfStockStatus();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<String>> getLowStockTitles() {
        return new ResponseEntity<>(comicService.findLowStockTitles(), HttpStatus.OK);
    }
}