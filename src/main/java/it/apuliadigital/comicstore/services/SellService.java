package it.apuliadigital.comicstore.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Sell sellComicWithSell(String title, int quantityToSell) {

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }

        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("La quantità da vendere deve essere maggiore di 0.");
        }

        Comic comic = comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

        if (comic.getPrice() == null) {
            throw new IllegalStateException("Il fumetto non ha un prezzo valido.");
        }

        int currentQuantity = comic.getQuantity();

        if (currentQuantity < quantityToSell) {
            throw new IllegalStateException("Copie insufficienti. Copie disponibili: " + currentQuantity + ".");
        }

        comic.setQuantity(currentQuantity - quantityToSell);
        comicRepository.save(comic);

        BigDecimal totalAmount = BigDecimal.valueOf(comic.getPrice())
                .multiply(BigDecimal.valueOf(quantityToSell));

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantityToSell);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(totalAmount);

        return sellRepository.save(sell);
    }

    public List<Sell> findSellsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("La data iniziale e la data finale sono obbligatorie.");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("La data iniziale non può essere successiva alla data finale.");
        }

        return sellRepository.findBySellingDateBetween(startDate, endDate);
    }

    public List<Sell> findSellsGreaterThanAmount(BigDecimal amount) {

        if (amount == null) {
            throw new IllegalArgumentException("L'importo è obbligatorio.");
        }

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("L'importo non può essere negativo.");
        }

        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}