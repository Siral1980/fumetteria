package it.apuliadigital.comicstore.services;

import java.math.BigDecimal;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;

@Service
public class ComicService {
    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SellRepository sellRepository;

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

public Sell sellComic(String title, int quantity) {
    Comic comic = comicRepository.findByTitleIgnoreCase(title)
            .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));

    if (comic.getQuantity() < quantity) {
        throw new IllegalArgumentException("Quantità insufficiente per vendere il fumetto: " + title);
    }
    Sell sell = new Sell();
    sell.setComic(comic);
    sell.setSellingDate(java.time.LocalDateTime.now());
    sell.setSellingQuantity(quantity);
    BigDecimal bigDecimal = new BigDecimal(String.valueOf(comic.getPrice() * quantity));
    sell.setTotalAmount(bigDecimal);
    comic.setQuantity(comic.getQuantity() - quantity);
    
    comicRepository.save(comic);
    return sellRepository.save(sell);
}

public List<Sell> findSalesByDateRange(LocalDate start, LocalDate end) {
    
    LocalDateTime startDateTime = start.atStartOfDay();
    LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();
    return sellRepository.findBySellingDateBetween(startDateTime, endDateTime);
}

public List<Sell> findSalesByAmountGreaterThan(BigDecimal amount) {
    return sellRepository.findByTotalAmountGreaterThan(amount);
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

}