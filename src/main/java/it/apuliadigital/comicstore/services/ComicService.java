package it.apuliadigital.comicstore.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.exceptions.ComicNotFoundException;
import it.apuliadigital.comicstore.exceptions.InsufficientStockException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SellRepository sellRepository;

    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        return comicRepository.save(comic);
    }

// GET ALL COMICS
public List<Comic> getAllComics() {
    return comicRepository.findAll();
}

    public Optional<Comic> findByTitle(String title) {
        return comicRepository.findByTitle(title);
    }

    public Comic stockComic(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        comic.setQuantity(comic.getQuantity() + quantity);
        return comicRepository.save(comic);
    }

    public Comic sellComic(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        if (comic.getQuantity() < quantity) {
            throw new InsufficientStockException("Copie disponibili insufficienti");
        }
        comic.setQuantity(comic.getQuantity() - quantity);
        return comicRepository.save(comic);
    }

    public Sell sellComicWithSell(Long id, int quantity) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        if (comic.getQuantity() < quantity) {
            throw new InsufficientStockException("Copie disponibili insufficienti");
        }
        comic.setQuantity(comic.getQuantity() - quantity);
        comicRepository.save(comic);

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(quantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(BigDecimal.valueOf(comic.getPrice() * quantity));
        return sellRepository.save(sell);
    }

    public Comic updateComic(Long id, Comic updatedComic) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException("Comic non trovato"));
        comic.setTitle(updatedComic.getTitle());
        comic.setAuthor(updatedComic.getAuthor());
        comic.setPrice(updatedComic.getPrice());
        comic.setGenre(updatedComic.getGenre());
        return comicRepository.save(comic);
    }

    public List<Sell> findSellsByDateRange(LocalDateTime start, LocalDateTime end) {
        return sellRepository.findBySellingDateBetween(start, end);
    }

    public List<Sell> findSellsByAmountGreaterThan(BigDecimal amount) {
        return sellRepository.findByTotalAmountGreaterThan(amount);
    }
}