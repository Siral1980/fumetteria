package it.apuliadigital.comicstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.apuliadigital.comicstore.dto.*;
import it.apuliadigital.comicstore.model.Comic;
import it.apuliadigital.comicstore.service.ComicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// controller per i fumetti, gestisce le richieste HTTP e le passa al service
// il controller non fa logica, chiama solo il service e restituisce la risposta
@RestController
@RequestMapping("/api/comics")
@RequiredArgsConstructor
@Tag(name = "Comics", description = "Gestione catalogo fumetti")
public class ComicController {

    private final ComicService comicService;

    // POST /api/comics - crea un nuovo fumetto
    // @Valid attiva le validazioni del DTO prima di entrare nel metodo
    // 201 Created è il codice corretto per una creazione
    @PostMapping
    @Operation(summary = "Aggiunge un nuovo fumetto (quantity = 0)")
    public ResponseEntity<Comic> addComic(@Valid @RequestBody ComicCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comicService.addComic(request));
    }

    // GET /api/comics/search?title=... - cerca per titolo esatto
    // uso @RequestParam per prendere il parametro dalla query string
    @GetMapping("/search")
    @Operation(summary = "Cerca un fumetto per titolo esatto")
    public ResponseEntity<Comic> findByTitle(@RequestParam String title) {
        return ResponseEntity.ok(comicService.findByTitle(title));
    }

    // PATCH /api/comics/{id}/stock - aggiunge copie in magazzino
    // uso PATCH perché modifico solo un campo, non l'intera risorsa
    @PatchMapping("/{id}/stock")
    @Operation(summary = "Aggiunge copie in magazzino")
    public ResponseEntity<Comic> stockComic(
            @PathVariable Long id,
            @Valid @RequestBody QuantityRequest request) {
        return ResponseEntity.ok(comicService.stockComic(id, request));
    }

    // PATCH /api/comics/{id}/sell - vende copie del fumetto
    // se non ci sono abbastanza copie il service lancia eccezione
    @PatchMapping("/{id}/sell")
    @Operation(summary = "Vende un fumetto (scala la quantità)")
    public ResponseEntity<Comic> sellComic(
            @PathVariable Long id,
            @Valid @RequestBody QuantityRequest request) {
        return ResponseEntity.ok(comicService.sellComic(id, request));
    }

    // PUT /api/comics/{id} - aggiorna tutti i dati del fumetto tranne id e quantity
    // PUT perché sostituisco i dati, non faccio una modifica parziale
    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna i dati del fumetto (esclusi id e quantity)")
    public ResponseEntity<Comic> updateComic(
            @PathVariable Long id,
            @Valid @RequestBody ComicUpdateRequest request) {
        return ResponseEntity.ok(comicService.updateComic(id, request));
    }

    // GET /api/comics/filter?query=... - ricerca parziale su titolo o autore
    // può tornare lista vuota, non 404
    @GetMapping("/filter")
    @Operation(summary = "Ricerca parziale per titolo o autore")
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String query) {
        return ResponseEntity.ok(comicService.findByFilter(query));
    }

    // PATCH /api/comics/toggle-out-of-stock - aggiorna outOfStock su tutti i fumetti
    // cicla il db e mette outOfStock a true se quantity == 0, false altrimenti
    @PatchMapping("/toggle-out-of-stock")
    @Operation(summary = "Aggiorna il flag outOfStock su tutto il database")
    public ResponseEntity<Map<String, Object>> toggleOutOfStock() {
        int total = comicService.toggleOutOfStock();
        // restituisco anche quanti fumetti sono stati processati
        return ResponseEntity.ok(Map.of(
                "message", "Toggle completato",
                "comicsProcessed", total
        ));
    }

    // GET /api/comics/out-of-stock - lista fumetti esauriti, solo titoli
    @GetMapping("/out-of-stock")
    @Operation(summary = "Restituisce i titoli dei fumetti esauriti")
    public ResponseEntity<List<ComicTitleResponse>> findOutOfStock() {
        return ResponseEntity.ok(comicService.findOutOfStock());
    }
}
