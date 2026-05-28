package it.apuliadigital.comicstore.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.models.Comic;

import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {
    @Autowired
    private ComicRepository comicRepository;


    public Comic addComic(Comic comic) {

          try {
      boolean isFound = comicRepository.existsByTitleAndAuthorAndGenreAndPrice(
        comic.getTitle(), 
        comic.getAuthor(), 
        comic.getGenre(), 
        comic.getPrice()
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
        return comicRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));
    }

   public Comic updateQuantityComic(String title, int quantity) {

    Comic comic = comicRepository.findByTitleIgnoreCase(title)
            .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));

    comic.setQuantity(comic.getQuantity() + quantity);
    
    return comicRepository.save(comic);
}

public Comic updateComic(
                           String title,
                           String author,
                           String genre,
                           double price
                           ) {
        // Stessa logica: fail-fast se la canzone non esiste
        Comic comic = getComicByTitle(title);

        // Controlli per i campi di tipo String
        if (author != null && !author.isBlank()) {
            comic.setAuthor(author);
        }
        if (genre != null && !genre.isBlank()) {
            comic.setGenre(genre);
        }
        // Controlli per i campi di tipo double
        if (price > 0) {
            comic.setPrice(price);
        }

        return comicRepository.save(comic);
    }

    public List<Comic> searchComics(String authorAndTitle) {
        return comicRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(authorAndTitle, authorAndTitle)
                .orElseThrow(() -> new IllegalArgumentException("Nessun fumetto trovato con autore: " + authorAndTitle + " o titolo: " + authorAndTitle));
    }

    public List<Comic> getAllComics() {
        return comicRepository.findAll();
    }

   
}