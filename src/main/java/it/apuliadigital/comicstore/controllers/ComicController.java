package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.services.ComicService;
import it.apuliadigital.comicstore.models.Comic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController 
@RequestMapping("/api/comics")
public class ComicController {

@Autowired
private ComicService comicService;

@PostMapping("/addComic")
public ResponseEntity<Comic> createComic(@RequestBody Comic comic) { 
    Comic savedComic = comicService.addComic(comic);
    return new ResponseEntity<>(savedComic, HttpStatus.CREATED);
}

@PostMapping("/toggle-stock")
public ResponseEntity<Void> toggleStock() {
    comicService.toggleAllOutOfStock();
    return ResponseEntity.ok().build();
}




@GetMapping("/title/{title}") //GET lettura
public ResponseEntity<Comic> getComicByTitle(@PathVariable String title) { //parametro nell'URL  
    Comic comic = comicService.findByTitle(title); //chiamiamo il servizio per trovare il fumetto con quel titolo
    return new ResponseEntity<>(comic, HttpStatus.OK); //HTTP 200 OK se lo troviamo, altrimenti il servizio lancerà un'eccezione che gestiremo globalmente

}

@GetMapping("/search")
public ResponseEntity<List<Comic>> searchComics(@RequestParam String keyword) { 
    List<Comic> filteredComics = comicService.findByFilter(keyword);
    return new ResponseEntity<>(filteredComics, HttpStatus.OK);
}

@GetMapping("/low-stock")
public ResponseEntity<List<String>> getLowStockNames() {
    List<String> names = comicService.findLowStockNames();
    return new ResponseEntity<>(names, HttpStatus.OK);
}

    @PutMapping("/{id}/stock") //indica che questo  gestisce le modifiche di dati esistenti
    public ResponseEntity<Comic> stockComic(@PathVariable Long id, @RequestParam int quantity) { // L'URL conterrà l'id del fumetto
        Comic updatedComic = comicService.stockComic(id, quantity); //Service passando l'id e il numero di copie da caricare
        return new ResponseEntity<>(updatedComic, HttpStatus.OK);// Stato HTTP 200 (OK)
    }

    @PutMapping("/{id}/sell") // Endpoint PUT per la vendita 
    public ResponseEntity<Comic> sellComic(@PathVariable Long id, @RequestParam int quantity) { // Chiamiamo il metodo del Service per gestire la logica della vendita
        Comic updatedComic = comicService.sellComic(id, quantity);
        return new ResponseEntity<>(updatedComic, HttpStatus.OK); // Restituiamo il fumetto con la quantità aggiornata che è minore e lo stato 200
    }
    // PUT per l'aggiornamento dei dati 
    @PutMapping("/{id}")
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comic) {
        Comic updatedComic = comicService.updateComic(id, comic); // Chiamiamo il metodo del Service che si occupa di proteggere ID e quantità e aggiornare il resto
        return new ResponseEntity<>(updatedComic, HttpStatus.OK); // Restituiamo il fumetto modificato con lo stato HTTP 200
    }









}