package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    // ADD COMIC
    @PostMapping
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic saved = comicService.addComic(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

  // GET ALL COMICS
@GetMapping
public ResponseEntity<List<Comic>> getAllComics() {
    List<Comic> comics = comicService.getAllComics();
    return ResponseEntity.ok(comics);
}

    // STOCK COMIC
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(comic);
    }

    // SELL COMIC
    @PatchMapping("/{id}/sell")
    public ResponseEntity<Comic> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        Comic comic = comicService.sellComic(id, quantity);
        return ResponseEntity.ok(comic);
    }

    // SELL COMIC WITH SELL RECORD
    @PostMapping("/{id}/sellWithRecord")
    public ResponseEntity<Sell> sellComicWithSell(@PathVariable Long id, @RequestParam int quantity) {
        Sell sell = comicService.sellComicWithSell(id, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sell);
    }

    // UPDATE COMIC
    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
        Comic updated = comicService.updateComic(id, comic);
        return ResponseEntity.ok(updated);
    }

    // CERCA VENDITE IN RANGE DI DATE
    @GetMapping("/sells/date-range")
    public ResponseEntity<List<Sell>> findSellsByDateRange(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        List<Sell> sells = comicService.findSellsByDateRange(start, end);
        return ResponseEntity.ok(sells);
    }

    // CERCA VENDITE SUPERIORI A UN IMPORTO
    @GetMapping("/sells/amount")
    public ResponseEntity<List<Sell>> findSellsByAmount(@RequestParam BigDecimal amount) {
        List<Sell> sells = comicService.findSellsByAmountGreaterThan(amount);
        return ResponseEntity.ok(sells);
    }
}