package it.apuliadigital.comicstore.service;

import it.apuliadigital.comicstore.dto.QuantityRequest;
import it.apuliadigital.comicstore.dto.SellResponse;
import it.apuliadigital.comicstore.model.Comic;
import it.apuliadigital.comicstore.model.Sell;
import it.apuliadigital.comicstore.repository.SellRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// service per gestire lo storico delle vendite
// usa anche ComicService per eseguire la vendita vera e propria
@Service
@RequiredArgsConstructor
@Slf4j
public class SellService {

    private final SellRepository sellRepository;
    // inietto ComicService perché ho bisogno di sellComic per scalare la quantità
    private final ComicService comicService;

    // vende il fumetto e crea il record nello storico
    // prima chiama sellComic che controlla lo stock e scala la quantità
    // poi salva la vendita con tutti i dettagli
    @Transactional
    public SellResponse sellComicHistory(Long comicId, QuantityRequest request) {
        // questo metodo si occupa già del controllo stock e di scalare la quantity
        Comic comic = comicService.sellComic(comicId, request);

        // calcolo il prezzo totale moltiplicando prezzo unitario per quantità
        BigDecimal totalPrice = comic.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        // creo il record della vendita con tutti i dati richiesti
        Sell sell = Sell.builder()
                .comic(comic)
                .quantitySold(request.getQuantity())
                .soldAt(LocalDateTime.now())  // timestamp automatico al momento della vendita
                .totalPrice(totalPrice)
                .build();

        sell = sellRepository.save(sell);
        log.debug("Vendita registrata: {} x{} = €{}", comic.getTitle(), request.getQuantity(), totalPrice);

        return toResponse(sell);
    }

    // cerca le vendite in un intervallo di date
    // from e to inclusi grazie a Between
    @Transactional(readOnly = true)
    public List<SellResponse> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return sellRepository.findBySoldAtBetween(from, to)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // cerca le vendite con importo totale superiore ad una certa soglia
    @Transactional(readOnly = true)
    public List<SellResponse> findByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalPriceGreaterThan(amount)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // converte l'entity Sell nel DTO di risposta
    // metto il titolo del fumetto invece dell'oggetto intero per evitare problemi di serializzazione
    private SellResponse toResponse(Sell sell) {
        return new SellResponse(
                sell.getId(),
                sell.getComic().getTitle(),
                sell.getQuantitySold(),
                sell.getSoldAt(),
                sell.getTotalPrice()
        );
    }
}
