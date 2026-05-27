package it.apuliadigital.comicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    @Autowired
    private ComicService comicService;

    @PostMapping("/addComic")
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {

        Comic createdComic = comicService.addComic(comic);

        return ResponseEntity.ok(createdComic);
    }

    @GetMapping("/findByTitle")
    public ResponseEntity<Comic> findComicByTitle(@RequestParam String title) {

        Comic comicFound = comicService.findComicByTitle(title);

        return ResponseEntity.ok(comicFound);
    }

    @PutMapping("/stockComic")
    public ResponseEntity<Comic> stockComic(@RequestParam String title, @RequestParam int quantity) {

        Comic updatedComic = comicService.stockComic(title, quantity);

        return ResponseEntity.ok(updatedComic);
    }

    @PutMapping("/sellComic")
    public ResponseEntity<Comic> sellComic(@RequestParam String title, @RequestParam int quantity) {

        Comic updatedComic = comicService.sellComic(title, quantity);

        return ResponseEntity.ok(updatedComic);
    }

    @PutMapping("/updateComic/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comicDetails) {

        Comic updatedComic = comicService.updateComic(id, comicDetails);

        return ResponseEntity.ok(updatedComic);
    }
}