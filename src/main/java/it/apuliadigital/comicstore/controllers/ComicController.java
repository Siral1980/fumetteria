package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comics")
public class ComicController {
    @Autowired
    public ComicService comicService;
    public SellService sellService;

    @PostMapping("/addComic")
    public ResponseEntity<Comic> addComic(@RequestParam String title,
                                          @RequestParam String author,
                                          @RequestParam double price,
                                          @RequestParam String genre) {
        Comic c = new Comic();
        c.setTitle(title);
        c.setAuthor(author);
        c.setPrice(price);
        c.setGenre(genre);
        c.setQuantity(0);
        return ResponseEntity.ok(comicService.addComic(c));
    }

    @PostMapping("/stockComic")
    public ResponseEntity<Optional<Comic>> stockComic(@RequestParam String title,
                                                      @RequestParam int quantity){
        return ResponseEntity.ok(comicService.stockComic(title, quantity));
    }


    @GetMapping("/getComicByTitle")
    public ResponseEntity<Optional<Comic>> findByTitle(@RequestParam String title){
        return ResponseEntity.ok(comicService.findByTitle(title));
    }
}
