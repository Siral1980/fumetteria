package it.apuliadigital.comicstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;

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

    /**
     * Find comics by partial filter on title or author
     */
    @GetMapping("/filter")
    @Operation(summary = "Find comics by filter", description = "Searches for comics by partial title or author")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String filter) {
        List<Comic> comics = comicService.findByFilter(filter);
        if (comics.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comics);
    }

    /**
     * Toggle out of stock status for all comics based on quantity
     */
    @PutMapping("/toggle-stock-status")
    @Operation(summary = "Toggle out of stock status", description = "Updates outOfStock field based on quantity for all comics")
    public ResponseEntity<Void> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all out of stock comic names
     */
    @GetMapping("/low-stock/names")
    @Operation(summary = "Find low stock comic names", description = "Retrieves names of all out of stock comics")
    public ResponseEntity<List<String>> findLowStock() {
        List<String> names = comicService.findLowStock();
        if (names.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(names);
    }

    /**
     * Restock all comics (set outOfStock to false)
     */
    @PutMapping("/restock")
    @Operation(summary = "Restock all comics", description = "Marks all comics as back in stock (outOfStock = false)")
    public ResponseEntity<Void> restockAll() {
        comicService.restockAll();
        return ResponseEntity.noContent().build();
    }
}

