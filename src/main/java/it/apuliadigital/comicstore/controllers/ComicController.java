package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    // 1. Endpoint per aggiungere un fumetto (POST /api/comics)
    @PostMapping
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic createdComic = comicService.addComic(comic);
        return new ResponseEntity<>(createdComic, HttpStatus.CREATED);
    }

    // 2. Endpoint per cercare un fumetto per titolo (GET /api/comics/search)
    @GetMapping("/search")
    public ResponseEntity<Comic> findComicByTitle(@RequestParam String title) {
        Comic comic = comicService.findComicByTitle(title);
        if (comic == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(comic, HttpStatus.OK);
    }

    // 3. Endpoint per aggiornare lo stock (PUT /api/comics/{id}/stock)
    @PutMapping("/{id}/stock")
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Comic updatedComic = comicService.stockComic(id, quantity);
            return new ResponseEntity<>(updatedComic, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 4. Endpoint per vendere un fumetto (PUT /api/comics/{id}/sell)
    @PutMapping("/{id}/sell")
    public ResponseEntity<?> sellComic(@PathVariable Long id, @RequestParam int quantity) {
        try {
            Comic updatedComic = comicService.sellComic(id, quantity);
            return new ResponseEntity<>(updatedComic, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // 5. Endpoint per aggiornare i dettagli di un fumetto (PUT /api/comics/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comicDetails) {
        try {
            Comic updatedComic = comicService.updateComic(id, comicDetails);
            return new ResponseEntity<>(updatedComic, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}