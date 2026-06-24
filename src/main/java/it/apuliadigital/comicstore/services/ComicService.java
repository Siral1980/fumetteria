package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public Comic addComic(Comic comic) {
        if (comic.getTitle() == null || comic.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
            .orElseThrow(() -> new EntityNotFoundException(
                "Comic not found with title: " + title
            ));
    }

    public List<Comic> findByFilter(String query) {
        String search = query == null ? "" : query.trim();
        if (search.isEmpty()) {
            return List.of();
        }
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(search, search);
    }

    public Comic stockComic(Long id, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity to add must be greater than 0");
        }

        Comic comic = comicRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + id));

        comic.setQuantity(comic.getQuantity() + quantityToAdd);
        comic.setOutOfStock(comic.getQuantity() <= 0);
        return comicRepository.save(comic);
    }

    public Sell sellComic(Long comicId, int sellingQuantity) {
        if (sellingQuantity <= 0) {
            throw new IllegalArgumentException("Selling quantity must be greater than 0");
        }

        Comic comic = comicRepository.findById(comicId)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + comicId));

        if (comic.getQuantity() < sellingQuantity) {
            throw new IllegalStateException(
                "Not enough copies available. Requested: " + sellingQuantity +
                ", Available: " + comic.getQuantity()
            );
        }

        Sell sell = new Sell();
        sell.setComic(comic);
        sell.setSellingQuantity(sellingQuantity);
        sell.setSellingDate(LocalDateTime.now());
        sell.setTotalAmount(comic.getPrice().multiply(BigDecimal.valueOf(sellingQuantity)));

        comic.setQuantity(comic.getQuantity() - sellingQuantity);
        comic.setOutOfStock(comic.getQuantity() <= 0);
        comic.getSells().add(sell);
        comicRepository.save(comic);

        return sell;
    }

    public Comic updateQuantity(Long id, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Comic comic = comicRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + id));

        comic.setQuantity(quantity);
        comic.setOutOfStock(quantity <= 0);
        return comicRepository.save(comic);
    }

    public Comic updateComic(Long id, Comic updatedComic) {
        Comic comic = comicRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comic not found: " + id));

        comic.setTitle(updatedComic.getTitle());
        comic.setAuthor(updatedComic.getAuthor());
        comic.setPublisher(updatedComic.getPublisher());
        comic.setYear(updatedComic.getYear());
        comic.setGenre(updatedComic.getGenre());
        comic.setPrice(updatedComic.getPrice());
        comic.setOutOfStock(comic.getQuantity() <= 0);
        return comicRepository.save(comic);
    }

    public List<Comic> toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        List<Comic> updated = allComics.stream()
            .peek(comic -> comic.setOutOfStock(comic.getQuantity() <= 0))
            .collect(Collectors.toList());
        return comicRepository.saveAll(updated);
    }

    public List<String> findLowStockTitles() {
        return comicRepository.findByOutOfStockTrue().stream()
            .map(Comic::getTitle)
            .collect(Collectors.toList());
    }
}
