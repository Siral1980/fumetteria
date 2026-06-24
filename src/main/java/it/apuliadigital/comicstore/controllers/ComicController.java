package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.SellRequest;
import it.apuliadigital.comicstore.models.StockRequest;
import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.services.SellService;
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

    @Autowired
    private SellService sellService;

    @PostMapping
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        return new ResponseEntity<>(comicService.addComic(comic), HttpStatus.CREATED);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Comic> findComicByTitle(@PathVariable String title) {
        return new ResponseEntity<>(comicService.findByTitle(title), HttpStatus.OK);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestBody StockRequest request) {
        return new ResponseEntity<>(comicService.stockComic(id, request.getQuantity()), HttpStatus.OK);
    }

    @PostMapping("/{id}/sell")
    public ResponseEntity<?> sellComic(@PathVariable Long id, @RequestBody SellRequest request) {
        return new ResponseEntity<>(sellService.sellComic(id, request.getQuantity()), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comicDetails) {
        return new ResponseEntity<>(comicService.updateComic(id, comicDetails), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String query) {
        return new ResponseEntity<>(comicService.findByFilter(query), HttpStatus.OK);
    }

    @PostMapping("/toggle-stock")
    public ResponseEntity<Void> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<String>> findLowStock() {
        return new ResponseEntity<>(comicService.findLowStock(), HttpStatus.OK);
    }
}
