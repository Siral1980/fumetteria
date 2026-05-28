package it.apuliadigital.comicstore.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.apuliadigital.comicstore.ComicTitleDto;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;

@RestController
@RequestMapping("/comics")
public class ComicController {
    @Autowired
    private ComicService comicService;

 @PostMapping("/addComic")
    public ResponseEntity<Comic> createComic(@RequestParam String title,
                                                @RequestParam String author,
                                                @RequestParam String genre,
                                                @RequestParam double price)
    {
        Comic comic = new Comic();
        comic.setTitle(title);
        comic.setAuthor(author);
        comic.setGenre(genre);
        comic.setPrice(price);
        Comic createdComic = comicService.addComic(comic);
        return ResponseEntity.ok(createdComic);
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<Comic> getComicByTitle(@RequestParam String title) {
        Comic comic = comicService.getComicByTitle(title);
        return ResponseEntity.ok(comic);
    }
    @PutMapping("/updateQuantity")
    public ResponseEntity<Comic> updateComicQuantity(@RequestParam String title, @RequestParam int quantity) {
        Comic comic = comicService.getComicByTitle(title);
        Comic updatedComic = comicService.updateQuantity(comic, quantity);
        return ResponseEntity.ok(updatedComic);
    }
    @PutMapping("/sellComic")
    public ResponseEntity<?> sellComic(@RequestParam String title,
                                       @RequestParam int quantity) {
        try {
            Comic updatedComic = comicService.sellComic(title, quantity);
            return ResponseEntity.ok(updatedComic);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/findByFilter")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam(required = false) String keyword) {
        List<Comic> results = comicService.findByFilter(keyword);
        return ResponseEntity.ok(results);
    }  

    @PutMapping("/outOfStockToggle")
    public ResponseEntity<List<Comic>> outOfStockToggle() {
        List<Comic> updatedComics = comicService.outOfStockToggle();
        return ResponseEntity.ok(updatedComics);
    }

@GetMapping("/findLowStock")
    public ResponseEntity<List<ComicTitleDto>> findLowStock() {
        List<ComicTitleDto> titles = comicService.findLowStock();
        return ResponseEntity.ok(titles);
    }
}


 