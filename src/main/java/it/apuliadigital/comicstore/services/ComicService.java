package it.apuliadigital.comicstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import java.util.NoSuchElementException;
@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;
    public  Comic addComic(Comic comic){
       if(comic == null){
              throw new IllegalArgumentException("i dati del fumetto sono obbligatori"); 
       }
       if(comic.getTitle() == null || comic.getTitle().isBlank()){
                throw new IllegalArgumentException("il titolo del fumetto è obbligatorio"); 
       }
        comic.setQuantity(0);
        return comicRepository.save(comic);
    }

    public Comic findComicByTitle(String title){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("il titolo da cercare è obbligatorio");
        }
        if (!comicRepository.findByTitle(title).isPresent()) {
            throw new NoSuchElementException("Fumetto non trovato.");
        }
        return comicRepository.findByTitle(title).get();
    }

    public Comic stockComic(String title, int quantityToAdd){
       if (title == null || title.isBlank()) { // Controllo titolo.
            throw new IllegalArgumentException("Il titolo del fumetto e obbligatorio.");
        }
 
        if (quantityToAdd <= 0) { // Non posso aggiungere 0 o numeri negativi.
            throw new IllegalArgumentException("La quantita da aggiungere deve essere maggiore di 0.");
        }
 
        Comic comic = comicRepository.findByTitle(title) // Cerco il fumetto tramite titolo.
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato.")); // Se non esiste, errore.
 
        int currentQuantity = comic.getQuantity(); // Leggo la quantita attuale.
 
        comic.setQuantity(currentQuantity + quantityToAdd); // Nuova quantita = attuale + quantita da aggiungere.
 
        return comicRepository.save(comic); // Salvo il fumetto aggiornato.
    }

    public Comic sellComic(String title, int quantityToSell) { // Metodo per vendere copie senza registrare la vendita nella tabella sell.
 
        if (title == null || title.isBlank()) { // Controllo titolo.
            throw new IllegalArgumentException("Il titolo del fumetto e obbligatorio.");
        }
 
        if (quantityToSell <= 0) { // Non posso vendere 0 o numeri negativi.
              throw new IllegalArgumentException("La quantita da vendere deve essere maggiore di 0.");
        }
 
        Comic comic = comicRepository.findByTitle(title) // Cerco il fumetto.
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));
 
        int currentQuantity = comic.getQuantity(); // Quantita disponibile prima della vendita.
 
        if (currentQuantity < quantityToSell) { // Se provo a vendere piu copie di quelle disponibili...
            throw new IllegalStateException("Copie insufficienti. Copie disponibili: " + currentQuantity + "."); // ...errore.
        }
 
        comic.setQuantity(currentQuantity - quantityToSell); // Sottraggo le copie vendute.
 
        return comicRepository.save(comic); // Salvo e restituisco il fumetto aggiornato.
    }
     public Comic updateComic(Long id, Comic comicDetails) { // Metodo per aggiornare un fumetto.
 
        if (comicDetails == null) { // Se non arrivano dati...
            throw new IllegalArgumentException("I dati del fumetto sono obbligatori.");
        }
 
        if (id == null) { // Se manca l'id nell'URL...
            throw new IllegalArgumentException("L'id del fumetto e obbligatorio.");
        }
 
        if (comicDetails.getTitle() == null || comicDetails.getTitle().isBlank()) { // Il titolo resta obbligatorio.
            throw new IllegalArgumentException("Il titolo del fumetto e obbligatorio.");
        }
 
        Comic comic = comicRepository.findById(id) // Cerco il fumetto usando l'id dell'URL.
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));
 
        comic.setTitle(comicDetails.getTitle()); // Aggiorno il titolo.
        comic.setAuthor(comicDetails.getAuthor()); // Aggiorno l'autore.
        comic.setPrice(comicDetails.getPrice()); // Aggiorno il prezzo.
        comic.setGenre(comicDetails.getGenre()); // Aggiorno il genere.
 
        // NON facciamo comic.setId(...), perche l'id non si deve cambiare.
        // NON facciamo comic.setQuantity(...), perche la quantita cambia solo con stockComic o sellComic.
 
        return comicRepository.save(comic); // Salvo il fumetto aggiornato.
    }
}
