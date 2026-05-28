package it.apuliadigital.comicstore.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {

    
    @Autowired
    private ComicRepository comicRepository;

   /*
     * Aggiunge un nuovo fumetto al catalogo inizializzandolo a zero copie.
     */
    public Comic addComic(Comic comic){
       // Controlli preventivi sui dati obbligatori
        if(comic == null){
            throw new IllegalArgumentException("I dati del fumetto sono obbligatori");
        }
        if(comic.getTitle() == null || comic.getTitle().isBlank()){
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio");
        }

        // Imposta i valori di default per un fumetto appena inserito
        comic.setQuantity(0);
        comic.setOutOfStock(true);

        // Salvataggio sul database
        return comicRepository.save(comic);
    }

    /*
      Cerca un fumetto nel database usando il titolo esatto.
    */
    public Comic findComicByTitle(String title){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("Il titolo da cercare è obbligatorio");
        }

        // Se il repository restituisce un Optional vuoto, lancia l'eccezione
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));
    }

    /*
      Filtra i fumetti cercando una corrispondenza parziale nel titolo o nell'autore.
    */
    public List<Comic> findComicsByFilter(String filter){
        if(filter == null || filter.isBlank()){
            throw new IllegalArgumentException("La stringa di ricerca è obbligatoria");
        }
        // Il filtro viene usato due volte: sia per il campo Title che per il campo Author
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(filter, filter);
    }

    /*
      Incrementa la quantità in magazzino di un fumetto esistente.
    */
    public Comic stockComic(String title, int quantityToAdd){
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("La quantità da aggiungere deve essere maggiore di 0.");
        }

        Comic comic = comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

        // Calcola la nuova quantità e aggiorna lo stato di disponibilità
        int currentQuantity = comic.getQuantity();
        comic.setQuantity(currentQuantity + quantityToAdd);
        comic.setOutOfStock(comic.getQuantity() == 0);

        return comicRepository.save(comic);
    }

    /*
      Registra la vendita di un fumetto riducendone la disponibilità.
     */
    public Comic sellComic(String title, int quantityToSell) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }
        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("La quantità da vendere deve essere maggiore di 0.");
        }

        Comic comic = comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

            // Verifica se ci sono abbastanza copie per completare la vendita
        int currentQuantity = comic.getQuantity();
        if (currentQuantity < quantityToSell) {
            throw new IllegalStateException("Copie insufficienti. Copie disponibili: " + currentQuantity + ".");
        }

        // Decrementa la quantità e, se arriva a zero, imposta outOfStock a true
        comic.setQuantity(currentQuantity - quantityToSell);
        comic.setOutOfStock(comic.getQuantity() == 0);

        return comicRepository.save(comic);
    }

    /*
     Sincronizza lo stato 'outOfStock' di tutti i fumetti in base alla quantità corrente.
    */
    @Transactional
    public List<Comic> toggleOutOfStock() {
        List<Comic> comics = comicRepository.findAll();

        // Ricalcola il flag per ogni singolo fumetto della lista
        for (Comic comic : comics) {
            comic.setOutOfStock(comic.getQuantity() == 0);
        }
        return comicRepository.saveAll(comics);
    }

    /*
     Estrae esclusivamente i titoli di tutti i fumetti che risultano esauriti.
    */
    public List<String> findLowStockTitles() {

        // Prende i fumetti esauriti, li trasforma in stream, isola il titolo e crea la lista finale
        return comicRepository.findByOutOfStockTrue().stream()
                .map(Comic::getTitle)
                .toList();
    }

    /*
      Aggiorna i dettagli anagrafici (Titolo, Autore, Prezzo, Genere) di un fumetto esistente tramite ID.
    */
    public Comic updateComic(Long id, Comic comicDetails) {
        if (comicDetails == null) {
            throw new IllegalArgumentException("I dati del fumetto sono obbligatori.");
        }
        if (id == null) {
            throw new IllegalArgumentException("L'id del fumetto è obbligatorio.");
        }
        if (comicDetails.getTitle() == null || comicDetails.getTitle().isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }

        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

        // Sovrascrive i vecchi dati con quelli nuovi forniti in input
        comic.setTitle(comicDetails.getTitle());
        comic.setAuthor(comicDetails.getAuthor());
        comic.setPrice(comicDetails.getPrice());
        comic.setGenre(comicDetails.getGenre());

        return comicRepository.save(comic);
    }
}
