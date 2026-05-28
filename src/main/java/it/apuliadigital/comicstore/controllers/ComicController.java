package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//Una bella classe dove vengono dichiarati tutti gli endpoint per creare e gestire i nostri
//Coccolosi comics

@RestController
@RequestMapping("/comics")

public class ComicController {
    @Autowired
    public ComicService comicService;
    public SellService sellService;

    //Metodo post per aggiungere un comic
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
        c.setOutOfStock(true);
        return ResponseEntity.ok(comicService.addComic(c));
    }
    //Metodo post per aggiungere quantità ad un comic
    @PutMapping("/stockComic")
    public ResponseEntity<Optional<Comic>> stockComic(@RequestParam String title,
                                                      @RequestParam int quantity){
        return ResponseEntity.ok(comicService.stockComic(title, quantity));
    }

    @PutMapping("/updateComic")
    public ResponseEntity<Optional<Comic>> updateComic(@RequestParam String title,
                                                       @RequestParam(required = false) String price,
                                                       @RequestParam(required = false) String author,
                                                       @RequestParam(required = false) String genre){
        return ResponseEntity.ok(comicService.updateComic(title, price, author, genre));
    }

    //Metodo get per visualizzare tutti i comics out of stock
    @GetMapping("/getLowStockComic")
    public ResponseEntity<List<String>> findLowStockComic(){
        return ResponseEntity.ok(comicService.getLowStockComics());
    }

    //Metodo get per prendere un comic dal titolo
    @GetMapping("/getComicByTitle")
    public ResponseEntity<Optional<Comic>> findByTitle(@RequestParam String title){
        return ResponseEntity.ok(comicService.findByTitle(title));
    }


    //Metodo per effettuare una ricerca filtrata (Per autore e titolo)
    @GetMapping("/filterSearch")
    public ResponseEntity<Optional<List<Comic>>> findAllSongsByFilter(@RequestParam(value = "filter", required = false, defaultValue = "")
                                                           String filter){

        return ResponseEntity.ok(comicService.findAllComicsByFilter(filter));

    }
}

