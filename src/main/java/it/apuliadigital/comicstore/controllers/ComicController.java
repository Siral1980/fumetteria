package it.apuliadigital.comicstore.controllers; // Pacchetto dei controller.
 
import org.springframework.beans.factory.annotation.Autowired; // Per inserire il service automaticamente.
import org.springframework.http.ResponseEntity; // Oggetto che rappresenta una risposta HTTP.
import org.springframework.web.bind.annotation.*; // Importa RestController, RequestMapping, GetMapping, ecc.
 
import it.apuliadigital.comicstore.models.Comic; // Model Comic.
import it.apuliadigital.comicstore.services.ComicService; // Service con la logica dei fumetti.
 
@RestController // Questa classe risponde con dati JSON, non con pagine HTML.
@RequestMapping("/api/comics") // Tutti gli endpoint iniziano con /api/comics.
public class ComicController {
 
    @Autowired // Spring inserisce automaticamente ComicService.
    private ComicService comicService;
 
    @PostMapping("/addComic") // Endpoint POST /api/comics/addComic.
    public ResponseEntity<Comic> addComic(@RequestBody Comic comic) { // Il fumetto arriva dal body JSON.
 
        Comic createdComic = comicService.addComic(comic); // Chiamo il service.
 
        return ResponseEntity.ok(createdComic); // Rispondo con 200 OK e il fumetto creato.
    }
 
    @GetMapping("/findByTitle") // Endpoint GET /api/comics/findByTitle?title=...
    public ResponseEntity<Comic> findComicByTitle(@RequestParam String title) { // title arriva dai parametri URL.
 
        Comic comicFound = comicService.findComicByTitle(title); // Chiamo il service.
 
        return ResponseEntity.ok(comicFound); // Restituisco il fumetto trovato.
    }
 
    @PutMapping("/stockComic") // Endpoint PUT /api/comics/stockComic?title=...&quantity=...
    public ResponseEntity<Comic> stockComic(@RequestParam String title, @RequestParam int quantity) {
 
        Comic updatedComic = comicService.stockComic(title, quantity); // Aggiungo copie.
 
        return ResponseEntity.ok(updatedComic); // Restituisco il fumetto aggiornato.
    }
     @PutMapping("/sellComic") // Endpoint PUT /api/comics/sellComic?title=...&quantity=...
    public ResponseEntity<Comic> sellComic(@RequestParam String title, @RequestParam int quantity) {
 
        Comic updatedComic = comicService.sellComic(title, quantity); // Vendo copie.
 
        return ResponseEntity.ok(updatedComic); // Restituisco il fumetto aggiornato.
    }
 
    @PutMapping("/updateComic/{id}") // Endpoint PUT /api/comics/updateComic/1.
    public ResponseEntity<Comic> updateComic(@PathVariable Long id, @RequestBody Comic comicDetails) {
 
        Comic updatedComic = comicService.updateComic(id, comicDetails); // Aggiorno il fumetto.
 
        return ResponseEntity.ok(updatedComic); // Restituisco il fumetto aggiornato.
    }
}
