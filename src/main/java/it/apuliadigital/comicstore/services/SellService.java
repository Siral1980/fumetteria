package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Sell;
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

    // Vendite in un range di date
    public List<Sell> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return sellRepository.findBySellingDateBetween(from, to);
    }

    // Vendite superiori ad un importo
    public List<Sell> findByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}
