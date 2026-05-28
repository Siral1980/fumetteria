package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;
import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private ComicRepository comicRepository;

    /**
     * Registra la vendita di un fumetto e aggiorna la quantità disponibile.
     *
     * @param comicId id del fumetto da vendere
     * @param quantityToSell numero di copie da vendere
     * @return oggetto Sell salvato con i dettagli della vendita
     */
    @Transactional
    public Sell sellComicHistory(Long comicId, int quantityToSell) {
        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("Error: quantity to sell must be greater than 0");
        }

        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new ResourceNotFoundException("Error: comic with ID " + comicId + " not found"));

        if (comic.getQuantity() < quantityToSell) {
            throw new IllegalArgumentException("Error: not enough copies. Available: " + comic.getQuantity());
        }

        double totalPriceDouble = comic.getPrice() * quantityToSell;
        BigDecimal totalPrice = BigDecimal.valueOf(totalPriceDouble);

        comic.setQuantity(comic.getQuantity() - quantityToSell);
        if (comic.getQuantity() <= 0) {
            comic.setOutOfStock(true);
        }
        comicRepository.save(comic);

        Sell sale = new Sell();
        sale.setComic(comic);
        sale.setSellingQuantity(quantityToSell);
        sale.setSellingDate(LocalDateTime.now());
        sale.setTotalAmount(totalPrice);

        return sellRepository.save(sale);
    }

    /**
     * Trova le vendite fatte tra due date.
     *
     * @param start data/ora di inizio
     * @param end data/ora di fine
     * @return elenco delle vendite in quell'intervallo
     */
    public List<Sell> findSalesInDateRange(LocalDateTime start, LocalDateTime end) {
        return sellRepository.findBySellingDateBetween(start, end);
    }

    /**
     * Trova le vendite con importo totale maggiore di un valore.
     *
     * @param amount importo minimo
     * @return elenco delle vendite con totale superiore al valore
     */
    public List<Sell> findSalesWithAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}