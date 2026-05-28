package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
// Controller che ci gestisce tutte le request delle vendite di un comic.
@RestController
@RequestMapping("/sell")
public class SellController {
    @Autowired
    public SellService sellService;
    public ComicService comicService;

    public SellController(ComicService comicService) {
        this.comicService = comicService;
    }

    @PostMapping("/sellComic")
    public ResponseEntity<Sell> sellComic(@RequestParam String title,
                                          @RequestParam int quantity){
        return ResponseEntity.ok(comicService.sellComic(title, quantity));
    }

    @GetMapping("/getSellsByDate")
    public ResponseEntity<Optional<List<Sell>>> getSellsByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
        return ResponseEntity.ok(sellService.findSellByDate(start, end));
    }
}
