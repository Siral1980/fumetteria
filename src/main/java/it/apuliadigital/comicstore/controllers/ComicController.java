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
    /**
     * Riceve i dati di un fumetto e lo salva nel sistema.
     * Restituisce il fumetto creato con i valori aggiornati.
     */
    public ResponseEntity<Comic> createComic(@RequestBody Comic comic) {
        String requestId = UUID.randomUUID().toString();
        Comic createdComic = comicService.addComic(comic, requestId);
        return ResponseEntity.ok(createdComic);
    }

    @GetMapping("/findByTitle")
    @Operation(summary = "Trova un fumetto per titolo")
    /**
     * Cerca un fumetto in base al titolo fornito.
     * Restituisce il fumetto corrispondente se trovato.
     */
    public ResponseEntity<Comic> getComicByTitle(@RequestParam String title) {
        Comic comic = comicService.findByTitle(title);
        return ResponseEntity.ok(comic);
    }

    @PutMapping("/stockComic")
    @Operation(summary = "Aggiungi stock a un fumetto")
    /**
     * Incrementa la quantità disponibile di un fumetto esistente.
     * Restituisce il fumetto aggiornato dopo l'operazione di stock.
     */
    public ResponseEntity<Comic> stockComic(@RequestParam Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.stockComic(id, quantity));
    }

    @PutMapping("/sellComic")
    @Operation(summary = "Vendi un fumetto")
    /**
     * Riduce la quantità di un fumetto venduto.
     * Restituisce il fumetto aggiornato con la nuova disponibilità.
     */
    public ResponseEntity<Comic> sellComic(@RequestParam Long id, @RequestParam int quantity) {
        return ResponseEntity.ok(comicService.sellComic(id, quantity));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Aggiorna i dati di un fumetto")
    /**
     * Aggiorna i campi di un fumetto esistente.
     * Restituisce il fumetto modificato con i nuovi dati.
     */
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic newComicData) {
        return ResponseEntity.ok(comicService.updateComic(id, newComicData));
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtra i fumetti per titolo o autore")
    /**
     * Cerca fumetti contenenti la parola chiave nel titolo o nell'autore.
     * Restituisce la lista dei fumetti corrispondenti.
     */
    public ResponseEntity<List<Comic>> findByFilter(@RequestParam String keyword) {
        return ResponseEntity.ok(comicService.findByFilter(keyword));
    }

    @PutMapping("/toggle-stock")
    @Operation(summary = "Aggiorna lo stato outOfStock di tutti i fumetti in base alla quantità")
    /**
     * Verifica lo stock di tutti i fumetti e aggiorna il flag outOfStock.
     * Restituisce una conferma di aggiornamento al termine dell'operazione.
     */
    public ResponseEntity<String> toggleOutOfStock() {
        comicService.toggleOutOfStock();
        return ResponseEntity.ok("Database outOfStock statuses updated successfully.");
    }

    @GetMapping("/low-stock")
    @Operation(summary = "Trova i titoli dei fumetti che sono esauriti")
    /**
     * Restituisce i titoli dei fumetti con quantità pari a zero.
     * Utile per identificare i prodotti da rifornire.
     */
    public ResponseEntity<List<String>> getLowStockTitles() {
        return ResponseEntity.ok(comicService.findLowStockTitles());
    }
}