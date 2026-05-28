package it.apuliadigital.comicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;

@RestController
@RequestMapping("/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    // Aggiungi fumetto
    @PostMapping("/addComic")
    public ResponseEntity<Comic> createComic(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String genre,
            @RequestParam double price) {
        Comic comic = new Comic();
        comic.setTitle(title);
        comic.setAuthor(author);
        comic.setGenre(genre);
        comic.setPrice(price);
        Comic createdComic = comicService.addComic(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComic);
    }

    // Ricerca per titolo
    @GetMapping("/findByTitle")
    public ResponseEntity<Comic> getComicByTitle(@RequestParam String title) {
        Comic comic = comicService.getComicByTitle(title);
        return ResponseEntity.ok(comic);
    }

    // Aggiornamento quantità in magazzino
    @PutMapping("/updateQuantity")
    public ResponseEntity<Comic> updateQuantityComic(
            @RequestParam String title,
            @RequestParam int quantity) {
        Comic updatedComic = comicService.updateQuantityComic(title, quantity);
        return ResponseEntity.ok(updatedComic);
    }

    // Vendita del fumetto
    @PutMapping("/sellComic")
    public ResponseEntity<Sell> sellComic(
            @RequestParam String title,
            @RequestParam int quantity) {
        Sell sell = comicService.sellComic(title, quantity);
        return ResponseEntity.ok(sell);
    }

    // Aggiorna dati fumetto
    @PutMapping("/updateComic")
    public ResponseEntity<Comic> updateComic(
            @RequestParam String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") double price) {
        Comic updatedComic = comicService.updateComic(title, author, genre, price);
        return ResponseEntity.ok(updatedComic);
    }

    //Ricerca per titolo o autore
    @GetMapping("/findByFilter")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String keyword) {
        List<Comic> comics = comicService.findByFilter(keyword);
        return ResponseEntity.ok(comics);
    }

    // Toggle outOfStock su tutti i fumetti
    @PutMapping("/toggleOutOfStock")
    public ResponseEntity<Void> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return ResponseEntity.ok().build();
    }

    // Lista dei titoli fumetti esauriti
    @GetMapping("/outOfStock")
    public ResponseEntity<List<String>> getOutOfStockTitles() {
        List<String> titles = comicService.findOutOfStockTitles();
        return ResponseEntity.ok(titles);
    }
}