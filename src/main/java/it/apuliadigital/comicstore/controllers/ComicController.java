package it.apuliadigital.comicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
                                           @RequestParam double price                                         
    ) {
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
    public ResponseEntity<Comic> updateQuantityComic(@RequestParam String title, @RequestParam int quantity) {
        Comic comic = comicService.getComicByTitle(title);
        Comic updatedComic = comicService.updateQuantityComic(comic, quantity);
        return ResponseEntity.ok(updatedComic);
    }
    @PutMapping("/sellComic")
public ResponseEntity<?> sellComic(@RequestParam String title,
                                   @RequestParam int quantity) {
    Comic comic = comicService.getComicByTitle(title);
    if (comic.getQuantity() < quantity) {
        return ResponseEntity.badRequest()
                .body("Not enough stock. Available: " + comic.getQuantity());
    }
    Comic soldComic = comicService.updateQuantityComic(comic, comic.getQuantity() - quantity);
    return ResponseEntity.ok(soldComic);
}

@PutMapping("/updateComic")
public ResponseEntity<Comic> updateComic(@RequestParam String title,
                                         @RequestParam String newTitle,
                                         @RequestParam String author,
                                         @RequestParam String genre,
                                         @RequestParam double price) {
    Comic comic = comicService.getComicByTitle(title);
    comic.setTitle(newTitle);
    comic.setAuthor(author);
    comic.setGenre(genre);
    comic.setPrice(price);
    Comic updatedComic = comicService.updateComic(comic);
    return ResponseEntity.ok(updatedComic);
}
}