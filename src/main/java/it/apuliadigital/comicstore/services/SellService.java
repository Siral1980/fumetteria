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

    @Autowired
    private ComicService comicService;

    /**
     * Sells a comic and creates a sell record
     */
    public Sell sellComicWithSell(Long comicId, int quantity) {
        // Get the comic and check availability
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Comic not found with id: " + comicId));

        if (comic.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + comic.getQuantity());
        }

        // Decrease quantity through comicService
        comicService.sellComic(comicId, quantity);

        // Create sell record
        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(BigDecimal.valueOf(comic.getPrice() * quantity));

        return sellRepository.save(sell);
    }

    /**
     * Find sales within a date range
     */
    public List<Sell> findSalesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return sellRepository.findSalesByDateRange(startDate, endDate);
    }

    /**
     * Find sales with amount greater than specified value
     */
    public List<Sell> findSalesByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findSalesByAmountGreaterThan(amount);
    }
}
