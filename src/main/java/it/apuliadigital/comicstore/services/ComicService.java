package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Il nostro bellissimo service che gestisce tutta la parte logica nel nostro comic

@Service
public class ComicService {
    @Autowired
    public ComicRepository comicRepository;
    @Autowired
    public SellService sellService;


    //Questa funzione serve per controllare se ci sono fumetti che hanno quantità 0, in base alla quantità
    //Imposta la variabile outofstock su true o su false.
    //Questo lo fà ad ogni avvio dell'applicazione.
    @EventListener(ApplicationReadyEvent.class)
    public void checkForOutOfStock() {
        for(Comic c : comicRepository.findAll()){
            c.setOutOfStock(c.getQuantity() <= 0);
            comicRepository.save(c);
        }
    }

    //Metodo per creare un comic
    public Comic addComic(Comic c){
        if(existsByTitle(c.getTitle())) throw new IllegalArgumentException("Errore: Titolo fumetto già presente.");
        return comicRepository.save(c);
    }

    //Metodo per aggiungere un comic allo stock
    public Optional<Comic> stockComic(String title, int quantity){
        if(quantity <= 0) throw new IllegalArgumentException("Errore: Quantità = o > di 0.");
        Optional<Comic> c = findByTitle(title);
        if(c.isEmpty()) throw new IllegalArgumentException("Errore: Fumetto non trovato.");
        c.get().setQuantity(c.get().getQuantity() + quantity);
        c.get().setOutOfStock(false);
        comicRepository.save(c.get());
        return c;
    }

    //Metodo per vendere un comic, per poi passare a sell service la gestione della vendita.
    public Sell sellComic(String title, int quantity){
        if(quantity <= 0) throw new IllegalArgumentException("Errore: Quantità < o = a 0.");
        Optional<Comic> c = findByTitle(title);
        if(c.isEmpty()) throw new IllegalArgumentException("Errore: Fumetto non trovato.");
        if(c.get().getQuantity() < quantity) throw new IllegalArgumentException("Errore: Quantità fumetto non disponibile");
        c.get().setQuantity(c.get().getQuantity() - quantity);
        if(c.get().getQuantity() == 0) c.get().setOutOfStock(true);
        comicRepository.save(c.get());
        return sellService.sellComicWithSell(c.get(), quantity);
    }

    //Controllo esistenza di un comic per titolo
    public boolean existsByTitle(String title){return comicRepository.existsByTitle(title);}


    //Metodo per catturare un comic per titolo. Laddove non esiste throwiamo una exception.
    public Optional<Comic> findByTitle(String title){
        Optional<Comic> comic = comicRepository.findByTitle(title);
        if(comic.isEmpty()) throw new IllegalArgumentException("Errore: Titolo comic non trovato");
        return comic;
    }

    //Stessa cosa di sopra, ma con i filtri per titolo e per autore
    public Optional<List<Comic>> findAllComicsByFilter(String filter){
        if(filter == null || filter.isBlank()) throw new IllegalArgumentException("Errore: Filter vuoto.");
        String sanitizedFilter = filter.trim();
        Optional<List<Comic>> comics = comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(sanitizedFilter, sanitizedFilter);
        if(comics.isEmpty()) throw new IllegalArgumentException("Errore: Nessun fumetto trovato con questi filtri.");
        return comics;
    }

    //Metodo per mostrare tutti i comics che sono in out of stocks
    public List<String> getLowStockComics(){
        Optional<List<Comic>> comics = comicRepository.findByOutOfStock(true);
        if(comics.isEmpty()) throw new ResourceNotFoundException("Errore: Nessun fumetto trovato in out of stock.");
        List<String> titleComics = new ArrayList<>();
        for(Comic c : comics.get()) titleComics.add(c.getTitle());
        return titleComics;
    }

}
