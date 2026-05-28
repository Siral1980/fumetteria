package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.services.ComicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/comics")
@Tag(name = "Comic Controller")
public class ComicController {

    @Autowired
    private ComicService comicService;

    @PostMapping("/addComic")
    @Operation(summary = "Aggiungi un nuovo fumetto")
    public ResponseEntity<Comic> createComic(@RequestBody Comic comic) {
        String requestId = UUID.randomUUID().toString();
        Comic createdComic = comicService.addComic(comic, requestId);
        return ResponseEntity.ok(createdComic);
    }

    @GetMapping("/findByTitle")
    @Operation(summary = "Trova un fumetto per titolo")
    public ResponseEntity<Comic> getComicByTitle(@RequestParam String title) {
        Comic comic = comicService.findByTitle(title);
        return ResponseEntity.ok(comic);
    }

    @PutMapping("/stockComic")
    @Operation(summary = "Aggiungi stock a un fumetto")
    public ResponseEntity<Comic> stockComic(@RequestParam Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.stockComic(id, quantity));
    }

    @PutMapping("/sellComic")
    @Operation(summary = "Vendi un fumetto")
    public ResponseEntity<Comic> sellComic(@RequestParam Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.sellComic(id, quantity));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Aggiorna i dati di un fumetto")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic newComicData) {
        return ResponseEntity.ok(comicService.updateComic(id, newComicData));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtra i fumetti per titolo o autore")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String keyword) {
        return ResponseEntity.ok(comicService.findByFilter(keyword));
    }

    @PutMapping("/toggle-stock")
    @Operation(summary = "Aggiorna lo stato outOfStock di tutti i fumetti in base alla quantità")
    public ResponseEntity<String> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return ResponseEntity.ok("Database outOfStock statuses updated successfully.");
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Trova i titoli dei fumetti che sono esauriti")
    public ResponseEntity<List<String>> getLowStockTitles() {
        return ResponseEntity.ok(comicService.findLowStockTitles());
    }
}