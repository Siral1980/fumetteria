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
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private ComicRepository comicRepository;

    // ========================================================================
    // BONUS TASK 11: Sell Comic History
    // Crea un record in Sell indicando: fumetto venduto, quantità venduta,
    // data e orario, e prezzo totale della vendita.
    // ========================================================================
    public Sell sellComicHistory(Long comicId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La quantità deve essere maggiore di 0");
        }

        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + comicId));

        if (comic.getQuantity() < quantity) {
            throw new IllegalArgumentException("Copie disponibili insufficienti. Disponibili: " + comic.getQuantity());
        }

        // Decrementa la quantità del fumetto
        comic.setQuantity(comic.getQuantity() - quantity);
        comicRepository.save(comic);

        // Crea il record di vendita
        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(BigDecimal.valueOf(comic.getPrice() * quantity));

        return sellRepository.save(sell);
    }

    // ========================================================================
    // BONUS TASK 11a: Ricerca vendite in un range di date
    // ========================================================================
    public List<Sell> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return sellRepository.findBySellingDateBetween(from, to);
    }

    // ========================================================================
    // BONUS TASK 11b: Ricerca vendite superiore ad un determinato importo
    // ========================================================================
    public List<Sell> findByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}
