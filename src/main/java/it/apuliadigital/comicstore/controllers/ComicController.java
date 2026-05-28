package it.apuliadigital.comicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComic);
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<Comic> getComicByTitle(@RequestParam String title) {
        Comic comic = comicService.getComicByTitle(title);
        return ResponseEntity.ok(comic);
}

    @PutMapping("/updateQuantity")
    public ResponseEntity<Comic> updateQuantityComic(@RequestParam String title, @RequestParam int quantity) {
       
        Comic updatedComic = comicService.updateQuantityComic(title, quantity);
        return ResponseEntity.ok(updatedComic);
    }

    @PutMapping("/restockComic")
    public ResponseEntity<Comic> restockComic(@RequestParam String title,  String author,  String genre, double price) {
        Comic updatedComic = comicService.updateComic(title, author, genre, price);
        return ResponseEntity.ok(updatedComic);
    }

    @GetMapping("/searchByAuthorAndTitle")
    public ResponseEntity<List<Comic>> searchComics(@RequestParam String authorandTitle) {
        List<Comic> comics = comicService.searchComics(authorandTitle);
        return ResponseEntity.ok(comics);
    }

    @PutMapping("/toggleOutOfStock")
    public ResponseEntity<List<Comic>> toggleOutOfStockStatus() {
        List<Comic> updatedComics = comicService.toggleOutOfStockStatus();
        return ResponseEntity.ok(updatedComics);
}
    @GetMapping("/getComicsOutOfStock")
    public ResponseEntity<List<Comic>> getComicsOutOfStock() {
        List<Comic> comics = comicService.getComicsOutOfStock();
        return ResponseEntity.ok(comics);
    }
}
