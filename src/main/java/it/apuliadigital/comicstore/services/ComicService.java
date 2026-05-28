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
import java.util.Optional;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SellRepository sellRepository;

    // Punto 2 - Add Comic
    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);

    }

    // Punto 2 - Find Comic by Title
    public Optional<Comic> findByTitle(String title) {
        return comicRepository.findByTitle(title);
    }

    public Comic stockComic(Long id, int quantityToAdd) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + id));
        comic.setQuantity(comic.getQuantity() + quantityToAdd);
        return comicRepository.save(comic);
    }

// Punto 4 - Sell Comic
public Sell sellComic(Long comicId, int quantityToSell) {
        Comic comic = comicRepository.findById(comicId)
                .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + comicId));

        if (comic.getQuantity() < quantityToSell) {
            throw new RuntimeException("Quantità disponibile insufficiente. Disponibili: " + comic.getQuantity());
        }

    // Aggiorna la quantità del fumetto
    comic.setQuantity(comic.getQuantity() - quantityToSell);
    comicRepository.save(comic);

    // Crea e salva la vendita
    Sell sell = new Sell();
    sell.setComic(comic);
    sell.setSellingDate(LocalDateTime.now());
    sell.setSellingQuantity(quantityToSell);
    sell.setTotalAmount(BigDecimal.valueOf(comic.getPrice() * quantityToSell));

    return sellRepository.save(sell);
}

// Punto 5 - Update Comic
public Comic updateComic(Long id, Comic updatedComic) {
    Comic existing = comicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Fumetto non trovato con id: " + id));

    // Aggiorna solo i campi consentiti, id e quantity restano intatti
    existing.setTitle(updatedComic.getTitle());
    existing.setAuthor(updatedComic.getAuthor());
    existing.setPrice(updatedComic.getPrice());
    existing.setGenre(updatedComic.getGenre());

    return comicRepository.save(existing);
}

// Punto 8 - Find By Filter
public List<Comic> findByFilter(String query) {
    return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
}

public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        allComics.forEach(comic -> comic.setOutOfStock(comic.getQuantity() == 0));
        comicRepository.saveAll(allComics);
    }

// Punto 10 - Find out of Stock
public List<String> findOutOfStock() {
        return comicRepository.findTitlesOutOfStock();
    }
}