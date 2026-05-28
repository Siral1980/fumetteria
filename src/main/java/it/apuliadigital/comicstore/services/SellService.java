package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//La nostra bellissima classe SellService che gestisce tutta la logica della vendita di un comic.

@Service
public class SellService {
    @Autowired
    public SellRepository sellRepository;


    //Questo metodo viene invocato all'interno di comicService e serve per registrare la vendita del
    //comic aggiungendo tutte le informazioni del caso come data di vendita, quantità ecc.
    public Sell sellComicWithSell(Comic c, int quantity){
        Sell s = new Sell();
        s.setComic(c);
        s.setSellingDate(LocalDateTime.now().toLocalDate());
        s.setSellingQuantity(quantity);
        s.setTotalAmount(BigDecimal.valueOf(c.getPrice() * quantity));
        sellRepository.save(s);
        return s;
    }

    //Metodo per trovare una vendita per range di date.
    public Optional<List<Sell>> findSellByDate(LocalDate start, LocalDate end){
        Optional<List<Sell>> sells = sellRepository.findBySellingDateBetween(start, end);
        if(sells.isEmpty()) throw new IllegalArgumentException("Errore: Nessuna vendita trovata per data.");
        return sells;
    }
}
