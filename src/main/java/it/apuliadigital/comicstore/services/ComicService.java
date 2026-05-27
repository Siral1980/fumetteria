package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    // 1. ADD COMIC
    public Comic addComic(Comic comic) {
        comic.setQuantity(0); 
        return comicRepository.save(comic);
    }

    // 2. FIND COMIC BY TITLE
    public Comic findComicByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Fumetto con titolo '" + title + "' non trovato."));
    }

    // 3. STOCK COMIC
    public Comic stockComic(Long id, int quantityToAdd) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con ID: " + id));
        comic.setQuantity(comic.getQuantity() + quantityToAdd);
        return comicRepository.save(comic);
    }

    // 4. SELL COMIC
    public Comic sellComic(Long id, int quantityToSell) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con ID: " + id));
        
        if (comic.getQuantity() < quantityToSell) {
            throw new RuntimeException("Disponibilità insufficiente. Copie presenti: " + comic.getQuantity());
        }
        
        comic.setQuantity(comic.getQuantity() - quantityToSell);
        return comicRepository.save(comic);
    }

    // 5. UPDATE COMIC
    public Comic updateComic(Long id, Comic incomingComic) {
        Comic existingComic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con ID: " + id));
        
        existingComic.setTitle(incomingComic.getTitle());
        existingComic.setAuthor(incomingComic.getAuthor());
        existingComic.setPrice(incomingComic.getPrice());
        
        return comicRepository.save(existingComic);
    }
}