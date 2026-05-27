package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.models.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    @PostMapping("/add")
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        String requestId = UUID.randomUUID().toString();
        Comic nuovoComic = comicService.addComic(comic, requestId);
        return ResponseEntity.ok(nuovoComic);
    }
    @GetMapping("/findComicByTitle/{title}")
    public ResponseEntity<Comic> findComicByTitle(@PathVariable String title) {
        Comic comic = comicService.findComicByTitle(title);
        return ResponseEntity.ok(comic);
    }
    @PatchMapping("/{title}/add-stock")
    public ResponseEntity<Comic> updateStock(@PathVariable String title, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.addStock(title, quantity));
    }

    @PatchMapping("/{title}/sell")
    public ResponseEntity<Comic> sellComic(
            @PathVariable String title,
            @RequestParam int quantity) {

        // Un piccolo controllo di validità dell'input
        if (quantity <= 0) {
            throw new IllegalArgumentException("The quantity to be sold must be at least 1");
        }

        Comic soldComic = comicService.sellComic(title, quantity);
        return ResponseEntity.ok(soldComic);
    }

}
