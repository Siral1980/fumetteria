 package it.apuliadigital.comicstore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {
    @Autowired
    private  ComicRepository comicRepository;

    public Comic addComic(Comic comic) {

          try {
      boolean isFound = comicRepository.existsByTitleAndAuthorAndGenreAndPriceAfterAllIgnoreCase(
        comic.getTitle(), 
        comic.getAuthor(), 
        comic.getGenre(), 
        comic.getPrice(),comic.getQuantity()
        
      );
      if (isFound) {
       throw new IllegalArgumentException("Il fumetto con lo stesso titolo, autore, genere, prezzo e quantità esiste già.");
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

    public Comic updateQuantityComic(Comic comic, int quantity) {
      comicRepository.findByTitleAllIgnoreCase(comic.getTitle())
        .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + comic.getTitle()));
        
      comic.setQuantity(comic.getQuantity() + quantity);
      return comicRepository.save(comic);
    }
   public Comic updateComic(Comic comic) {
    return comicRepository.save(comic);
}
}


