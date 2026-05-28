
package it.apuliadigital.comicstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/findByFilter")
    public ResponseEntity<List<Comic>> findComicsByFilter(@RequestParam String filter) {

        List<Comic> comics = comicService.findComicsByFilter(filter);

        return ResponseEntity.ok(comics);
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

    @PutMapping("/toggleOutOfStock")
    public ResponseEntity<List<Comic>> toggleOutOfStock() {

        List<Comic> updatedComics = comicService.toggleOutOfStock();

        return ResponseEntity.ok(updatedComics);
    }

    @GetMapping("/findLowStock")
    public ResponseEntity<List<String>> findLowStock() {

        List<String> outOfStockTitles = comicService.findLowStock();

        return ResponseEntity.ok(outOfStockTitles);
    }
}