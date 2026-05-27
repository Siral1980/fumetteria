package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SellRepository sellRepository;

    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        return comicRepository.save(comic);
    }

    public Comic findComicByTitle(String title) {
        return comicRepository.findByTitle(title).orElse(null);
    }

    public Comic stockComic(Long id, int quantityToAdd) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto con ID " + id + " non trovato"));
        comic.setQuantity(comic.getQuantity() + quantityToAdd);
        return comicRepository.save(comic);
    }

    public Comic sellComic(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con ID: " + id));

        if (comic.getQuantity() < quantity) {
            throw new IllegalArgumentException("Quantità insufficiente in magazzino! Disponibili: " + comic.getQuantity());
        }

        comic.setQuantity(comic.getQuantity() - quantity);
        return comicRepository.save(comic);
    }

    public Comic updateComic(Long id, Comic comicDetails) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con ID: " + id));

        comic.setTitle(comicDetails.getTitle());
        comic.setAuthor(comicDetails.getAuthor());
        comic.setGenre(comicDetails.getGenre());
        comic.setPrice(comicDetails.getPrice());

        return comicRepository.save(comic);
    }

    // ==========================================
    // BONUS TASK: LOGICA REGISTRAZIONE VENDITE
    // ==========================================

    public Sell sellComicWithSell(Long id, int quantity) {
        Comic comic = sellComic(id, quantity);

        // Calcolo preciso del prezzo totale con BigDecimal
        double comicPrice = comic.getPrice() != null ? comic.getPrice() : 0.0;
        BigDecimal totalAmount = BigDecimal.valueOf(comicPrice).multiply(BigDecimal.valueOf(quantity));

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(totalAmount);

        return sellRepository.save(sell);
    }

    public List<Sell> findSalesInDateRange(LocalDateTime start, LocalDateTime end) {
        return sellRepository.findBySellingDateBetween(start, end);
    }

    public List<Sell> findSalesWithPriceGreaterThan(double amount) {
        return sellRepository.findByTotalAmountGreaterThan(BigDecimal.valueOf(amount));
    }
}