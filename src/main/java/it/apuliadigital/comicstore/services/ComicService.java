package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.controllers.SellController;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ComicService {
    @Autowired
    public ComicRepository comicRepository;
    public SellService sellService;


    public Comic addComic(Comic c){
        if(existsByTitle(c.getTitle())) throw new IllegalArgumentException("Errore: Titolo fumetto già presente.");
        return comicRepository.save(c);
    }

    public Optional<Comic> stockComic(String title, int quantity){
        Optional<Comic> c = findByTitle(title);
        if(c.isEmpty()) throw new IllegalArgumentException("Errore: Fumetto non trovato.");
        c.get().setQuantity(c.get().getQuantity() + quantity);
        comicRepository.save(c.get());
        return c;
    }

    public Sell sellComic(String title, int quantity){
        Optional<Comic> c = findByTitle(title);
        if(c.isEmpty()) throw new IllegalArgumentException("Errore: Fumetto non trovato.");
        if(c.get().getQuantity() < quantity) throw new IllegalArgumentException("Errore: Quantità fumetto non disponibile");
        c.get().setQuantity(c.get().getQuantity() - quantity);
        comicRepository.save(c.get());
        return sellService.sellComicWithSell(c.get());
    }

    public boolean existsByTitle(String title){return comicRepository.existsByTitle(title);}

    public Optional<Comic> findByTitle(String title){
        Optional<Comic> comic = comicRepository.findByTitle(title);
        if(comic.isEmpty()) throw new IllegalArgumentException("Errore: Titolo comic non trovato");
        return comic;
    }

}
