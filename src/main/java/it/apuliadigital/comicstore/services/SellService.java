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

public List<Sell> findAllSales() {
    return sellRepository.findAll();
}
}
