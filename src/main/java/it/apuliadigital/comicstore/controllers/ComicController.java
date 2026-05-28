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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
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
    
    @PutMapping("/sellComic")
    public ResponseEntity<Sell> sellComic(@RequestParam String title, @RequestParam int quantity) {
        Sell sell = comicService.sellComic(title, quantity);
        return ResponseEntity.ok(sell);
    }

    @PutMapping("/restockComic")
    public ResponseEntity<Comic> restockComic(@RequestParam String title,  String author,  String genre, double price) {
        Comic updatedComic = comicService.updateComic(title, author, genre, price);
        return ResponseEntity.ok(updatedComic);
    }

    @GetMapping("/sales/byDate")
    public ResponseEntity<List<Sell>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<Sell> sales = comicService.findSalesByDateRange(start, end);
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/sales/byAmount")
    public ResponseEntity<List<Sell>> getSalesByAmount(
            @RequestParam BigDecimal amount) {
        List<Sell> sales = comicService.findSalesByAmountGreaterThan(amount);
        return ResponseEntity.ok(sales);
    }

}