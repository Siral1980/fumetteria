package it.apuliadigital.comicstore.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    public Comic addComic(Comic comic) {
        try{
            boolean isFound = comicRepository.existsByTitleAndAuthorAndGenreAndPriceAllIgnoreCase(
                comic.getTitle(),
                comic.getAuthor(),
                comic.getGenre(),
                comic.getPrice()
            );
            if(isFound){
                throw new IllegalArgumentException("Il fumetto esiste già nel database.");
            }
            comic.setQuantity(0);
            return comicRepository.save(comic);
        } catch (IllegalArgumentException e) {
            System.err.println("Errore: " + e.getMessage());
            throw e; 
}
    }
  public Comic getComicByTitle(String title) {
        return comicRepository.findByTitleAllIgnoreCase(title)
                .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));
  }
    public Comic updateQuantity(Comic comic, int quantity) {
        getComicByTitle(comic.getTitle());
        comic.setQuantity(comic.getQuantity() + quantity);
        return comicRepository.save(comic);
    }
        public Comic sellComic(String title, int quantity) {
        Comic comic = comicRepository.findByTitleAllIgnoreCase(title)  
                .orElseThrow(() -> new IllegalArgumentException(
                    "Fumetto '" + title + "' non trovato nel database."
                ));

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                "La quantità da vendere deve essere maggiore di zero."
            );
        }
        if (comic.getQuantity() < quantity) {
            throw new IllegalArgumentException(
                "Copie disponibili insufficienti. Disponibili: " 
                + comic.getQuantity() + ", Richieste: " + quantity
            );
        }

        comic.setQuantity(comic.getQuantity() - quantity);
        return comicRepository.save(comic);
    }
    public List<Comic> findByFilter(String keyword) {
        if(keyword == null || keyword.isBlank()){
            return comicRepository.findAll();
        }
        String cleanedKeyword = keyword.trim();
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingAllIgnoreCase(
            cleanedKeyword,
            cleanedKeyword
        );
    }
public List<Comic>  outOfStockToggle() {
    List<Comic> allComics = comicRepository.findAll();
    allComics.forEach(comic -> {
        if (comic.getQuantity() > 0) {
            comic.setOutOfStock(false);
        } else {
            comic.setOutOfStock(true);
        }
    });
    return comicRepository.saveAll(allComics);
    }
 }