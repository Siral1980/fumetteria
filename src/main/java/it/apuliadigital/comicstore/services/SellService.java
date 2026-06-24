package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.SellRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SellService {

    private final SellRepository sellRepository;

    public SellService(SellRepository sellRepository) {
        this.sellRepository = sellRepository;
    }

    public List<Sell> findSalesByDateRange(LocalDateTime from, LocalDateTime to) {
        return sellRepository.findBySellingDateBetween(from, to);
    }

    public List<Sell> findSalesAboveAmount(BigDecimal minAmount) {
        return sellRepository.findByTotalAmountGreaterThan(minAmount);
    }
}