package it.apuliadigital.comicstore.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private ComicRepository comicRepository;

    //sellComicHistory
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

    // Ricerca vendite in un range di date
    public List<Sell> findSalesByDateRange(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.plusDays(1).atStartOfDay();
        return sellRepository.findBySellingDateBetween(startDateTime, endDateTime);
    }

    // Ricerca vendite superiori ad un determinato importo
    public List<Sell> findSalesByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}