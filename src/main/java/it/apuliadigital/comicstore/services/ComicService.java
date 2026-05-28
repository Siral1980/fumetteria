package it.apuliadigital.comicstore.services; // Pacchetto dei service.
 
import java.util.NoSuchElementException; // Errore usato quando un fumetto non viene trovato.
 
import org.springframework.beans.factory.annotation.Autowired; // Serve per far inserire a Spring un oggetto gia pronto.
import org.springframework.stereotype.Service; // Dice a Spring che questa classe e un service.
import it.apuliadigital.comicstore.models.Comic; // Usiamo il model Comic.
import it.apuliadigital.comicstore.repositories.ComicRepository; // Usiamo il repository di Comic.
 
@Service // Spring deve creare e gestire questa classe come service.
public class ComicService {
 
    @Autowired // Spring inserisce automaticamente un ComicRepository dentro questa variabile.
    private ComicRepository comicRepository; // Oggetto che useremo per parlare con il database.
 
    public Comic addComic(Comic comic) { // Metodo che aggiunge un nuovo fumetto.
 
        if (comic == null) { // Se non arriva nessun fumetto...
            throw new IllegalArgumentException("I dati del fumetto sono obbligatori."); // ...lanciamo errore 400.
        }
 
        if (comic.getTitle() == null || comic.getTitle().isBlank()) { // Se il titolo manca o e vuoto...
            throw new IllegalArgumentException("Il titolo del fumetto e obbligatorio."); // ...lanciamo errore.
        }
 
        comic.setQuantity(0); // Regola del compito: un fumetto nuovo nasce con quantita 0.
 
        return comicRepository.save(comic); // Salviamo il fumetto nel database e lo restituiamo.
    }
    public Comic findComicByTitle(String title) { // Metodo che cerca un fumetto tramite titolo.
 
        if (title == null || title.isBlank()) { // Se il titolo da cercare non e valido...
            throw new IllegalArgumentException("Il titolo da cercare e obbligatorio."); // ...errore 400.
        }
 
        return comicRepository.findByTitle(title) // Chiediamo al database di cercare il titolo.
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato.")); // Se non lo trova, errore 404.
    }
 
    public Comic stockComic(String title, int quantityToAdd) { // Metodo per aggiungere copie in magazzino.
 
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
 
        if (quantityToSell <= 0) { // Non posso vendere 0 o numeri negativi.throw new IllegalArgumentException("La quantita da vendere deve essere maggiore di 0.");
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