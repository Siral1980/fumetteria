package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.exceptions.BadRequestException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellService {

    @Autowired
    private SellRepository sellRepository;

    @Autowired
    private ComicService comicService;

    @Transactional
    public Sell sellComic(Long comicId, int quantityToSell) {
        if (quantityToSell <= 0) {
            throw new BadRequestException("Quantity to sell must be greater than 0");
        }

        Comic comic = comicService.findById(comicId);

        if (comic.getQuantity() < quantityToSell) {
            throw new BadRequestException("Not enough copies in stock to sell");
        }

        comic.setQuantity(comic.getQuantity() - quantityToSell);
        if (comic.getQuantity() == 0) {
            comic.setOutOfStock(true);
        }
        comicService.saveComic(comic);

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantityToSell);
        sell.setSellingDate(LocalDateTime.now());
        Double price = comic.getPrice() != null ? comic.getPrice() : 0.0;
        sell.setTotalAmount(BigDecimal.valueOf(price * quantityToSell));

        return sellRepository.save(sell);
    }

    public List<Sell> findSellsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return sellRepository.findBySellingDateBetween(startDate, endDate);
    }

    public List<Sell> findSellsByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}
