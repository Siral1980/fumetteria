package it.apuliadigital.comicstore.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;


@Service  //business 
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;
    

    public Comic addComic (Comic comic) {
        comic.setQuantity(0);  //forzato a 0
        comic.setOutOfStock(true); //forzato a true
        return comicRepository.save(comic);    //salviamo il fumetto e lo restituiamo oggetto
    }
  

    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fumetto non trovato con il titolo: " + title));
    }    //facciamo un'eccezione se non troviamo il fumetto con quel titolo



    
    public Comic stockComic(Long id, int quantityToAdd) {
        Comic comic = comicRepository.findById(id)// Cerchiamo il fumetto tramite l' ID e se non esiste diamo un'eccezione per bloccare l'operazione
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile fare stock: Fumetto non trovato con ID: " + id));
        comic.setQuantity(comic.getQuantity() + quantityToAdd);// Calcoliamo la nuova quantità sommandola a quella attuale 
        if (comic.getQuantity() > 0) {
            comic.setOutOfStock(false);
        }
        return comicRepository.save(comic);// Salviamo le modifiche  restituiamo il fumetto aggiornato
    }

    // Quando vendi le copie devi scalarle dal magazzino
    public Comic sellComic(Long id, int quantityToSell) {
        Comic comic = comicRepository.findById(id) // Cerchiamo il fumetto tramite il suo ID 
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile vendere: Fumetto non trovato con ID: " + id));
        
        if (comic.getQuantity() < quantityToSell) { // Controlliamo se le copie disponibili sono sufficienti per la vendita
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operazione annullata: Copie insufficienti in magazzino! Disponibili: " + comic.getQuantity());
        } // Se la quantità in magazzino è minore di quella richiesta blocchiamo tutto lanciando un errore
        
        comic.setQuantity(comic.getQuantity() - quantityToSell); // Se la richiesta va a buon fine calcoliamo la nuova quantità togliendo i fumetti venduti
        
        if (comic.getQuantity() == 0) { // Se dopo la vendita la quantità è zero, aggiorniamo lo stato del fumetto a "esaurito"
            comic.setOutOfStock(true);
        }
        return comicRepository.save(comic); // Salviamo le modifiche e restituiamo il fumetto aggiornato
    }

    public Comic updateComic(Long id, Comic updateData) { // Aggiorniamo i dati generali di un fumetto senza toccare ID e quantità
        Comic existingComic = comicRepository.findById(id) // Cerchiamo il fumetto tramite il suo ID
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Impossibile aggiornare: Fumetto non trovato con ID: " + id));
        
        existingComic.setTitle(updateData.getTitle()); // Aggiorniamo i campi modificabili
        existingComic.setAuthor(updateData.getAuthor());
         

        return comicRepository.save(existingComic); //Salviamo le modifiche apportate e restituiamo il fumetto modificato
    }
 public List<Comic> findByFilter(String keyword) {
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }


    public void toggleAllOutOfStock () {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            if (comic.getQuantity() == 0) {
                comic.setOutOfStock(true);
            } else {
                comic.setOutOfStock(false);
            }
        }
        comicRepository.saveAll(allComics);
    }
   
public List<String> findLowStockNames() {
    List<Comic> outOfStockComics = comicRepository.findByOutOfStockTrue();
    return outOfStockComics.stream()
            .map(Comic::getTitle)
            .collect(Collectors.toList());

}}
