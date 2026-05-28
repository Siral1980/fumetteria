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
        boolean isFound = comicRepository.existsByTitleAndAuthorAndGenreAndPrice(
                comic.getTitle(),
                comic.getAuthor(),
                comic.getGenre(),
                comic.getPrice()
        );
        if (isFound) {
            throw new IllegalArgumentException("Il fumetto con lo stesso titolo, autore, genere e prezzo esiste già.");
        }
        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    // Ricerca per titolo
    public Comic getComicByTitle(String title) {
        return comicRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));
    }

    // Aggiunta quantità in magazzino
    public Comic updateQuantityComic(String title, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità da aggiungere deve essere un valore positivo.");
        }
        Comic comic = comicRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));

        comic.setQuantity(comic.getQuantity() + quantity);
        comic.setOutOfStock(comic.getQuantity() == 0);
        return comicRepository.save(comic);
    }

    // Vendita fumetto
    public Sell sellComic(String title, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità da vendere deve essere un valore positivo.");
        }
        Comic comic = comicRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));

        if (comic.getQuantity() < quantity) {
            throw new IllegalArgumentException("Quantità insufficiente per vendere il fumetto: " + title);
        }

        BigDecimal totalAmount = BigDecimal.valueOf(comic.getPrice())
                .multiply(BigDecimal.valueOf(quantity));

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingDate(LocalDateTime.now());
        sell.setSellingQuantity(quantity);
        sell.setTotalAmount(totalAmount);

        comic.setQuantity(comic.getQuantity() - quantity);
        comic.setOutOfStock(comic.getQuantity() == 0);

        comicRepository.save(comic);
        return sellRepository.save(sell);
    }

    //  Aggiornamento dati fumetto 
    public Comic updateComic(String title, String author, String genre, double price) {
        Comic comic = getComicByTitle(title);
        if (author != null && !author.isBlank()) comic.setAuthor(author);
        if (genre != null && !genre.isBlank()) comic.setGenre(genre);
        if (price > 0) comic.setPrice(price);
        return comicRepository.save(comic);
    }

    //  Ricerca parziale su titolo o autore
    public List<Comic> findByFilter(String keyword) {
        return comicRepository
                .findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    // Toggle outOfStock su tutti i fumetti in base alla quantity
    public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            comic.setOutOfStock(comic.getQuantity() == 0);
        }
        comicRepository.saveAll(allComics);
    }

    //  Restituisce solo i titoli dei fumetti esauriti
    public List<String> findOutOfStockTitles() {
        return comicRepository.findByOutOfStockTrue()
                .stream()
                .map(Comic::getTitle)
                .toList();
    }

    // sellComicHistory
    public Sell sellComicHistory(String title, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità da vendere deve essere un valore positivo.");
        }
        Comic comic = comicRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new IllegalArgumentException("Fumetto non trovato con il titolo: " + title));

        if (comic.getQuantity() < quantity) {
            throw new IllegalArgumentException("Quantità insufficiente per vendere il fumetto: " + title);
        }

        BigDecimal totalAmount = BigDecimal.valueOf(comic.getPrice())
                .multiply(BigDecimal.valueOf(quantity));

        Sell sell = new Sell();
        sell.setComic(comic);                     
        sell.setSellingQuantity(quantity);        
        sell.setSellingDate(LocalDateTime.now()); 
        sell.setTotalAmount(totalAmount);         

        comic.setQuantity(comic.getQuantity() - quantity);
        comic.setOutOfStock(comic.getQuantity() == 0);

        comicRepository.save(comic);
        return sellRepository.save(sell);
    }

    // Ricerca vendite per range di date
    public List<Sell> findSalesByDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();
        return sellRepository.findBySellingDateBetween(startDateTime, endDateTime);
    }

    // Ricerca vendite per importo superiore
    public List<Sell> findSalesByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}