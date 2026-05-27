package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/comics")
@Tag(name = "Comic Management", description = "APIs for managing comics in the store")
public class ComicController {

    @Autowired
    private ComicService comicService;

    /**
     * Add a new comic (quantity = 0 by default)
     */
    @PostMapping
    @Operation(summary = "Add a new comic", description = "Creates a new comic with quantity = 0")
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) {
        Comic newComic = comicService.addComic(comic);
        return new ResponseEntity<>(newComic, HttpStatus.CREATED);
    }

    /**
     * Find comic by title
     */
    @GetMapping("/search")
    @Operation(summary = "Find comic by title", description = "Searches for a comic using its unique title")
    public ResponseEntity<Comic> findComicByTitle(@RequestParam String title) {
        return comicService.findComicByTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get comic by id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get comic by id", description = "Retrieves a comic by its ID")
    public ResponseEntity<Comic> getComicById(@PathVariable Long id) {
        return comicService.getComicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Add comics to stock
     */
    @PutMapping("/{id}/stock")
    @Operation(summary = "Add comics to stock", description = "Increases the quantity of a comic in stock")
    public ResponseEntity<Comic> stockComic(
            @PathVariable Long id,
            @RequestParam int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Comic updatedComic = comicService.stockComic(id, quantity);
        return ResponseEntity.ok(updatedComic);
    }

    /**
     * Sell a comic
     */
    @PutMapping("/{id}/sell")
    @Operation(summary = "Sell a comic", description = "Decreases the quantity of a comic after selling")
    public ResponseEntity<Comic> sellComic(
            @PathVariable Long id,
            @RequestParam int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Comic updatedComic = comicService.sellComic(id, quantity);
        return ResponseEntity.ok(updatedComic);
    }

    /**
     * Update comic details (without changing id or quantity)
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update comic", description = "Updates comic details (title, author, price, genre) without changing id or quantity")
    public ResponseEntity<Comic> updateComic(
            @PathVariable Long id,
            @RequestBody Comic comicDetails) {
        Comic updatedComic = comicService.updateComic(id, comicDetails);
        return ResponseEntity.ok(updatedComic);
    }
}

