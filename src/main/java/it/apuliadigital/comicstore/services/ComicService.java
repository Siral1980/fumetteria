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
                throw new IllegalArgumentException("Il fumetto con lo stesso titolo, autore, genere e prezzo esiste già.");
            }
            comic.setQuantity(0);
            comic.setOutOfStock(true); // Task 9
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
        sell.setSellingDate(LocalDateTime.now());
        sell.setSellingQuantity(quantity);
        BigDecimal totalAmount = new BigDecimal(String.valueOf(comic.getPrice() * quantity));
        sell.setTotalAmount(totalAmount);

        comic.setQuantity(comic.getQuantity() - quantity);
        comic.setOutOfStock(comic.getQuantity() == 0); // Task 9

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

    public Comic updateComic(String title, String author, String genre, double price) {
        Comic comic = getComicByTitle(title);
        if (author != null && !author.isBlank()) comic.setAuthor(author);
        if (genre != null && !genre.isBlank()) comic.setGenre(genre);
        if (price > 0) comic.setPrice(price);
        return comicRepository.save(comic);
    }

    
    public List<Comic> findByFilter(String keyword) {
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    
    public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            comic.setOutOfStock(comic.getQuantity() == 0);
        }
        comicRepository.saveAll(allComics);
    }

    
    public List<String> findOutOfStockTitles() {
        List<Comic> outOfStock = comicRepository.findByOutOfStockTrue();
        return outOfStock.stream()
                .map(Comic::getTitle)
                .toList();
    }
}