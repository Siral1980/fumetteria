package it.apuliadigital.comicstore.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    public Comic addComic(Comic comic) {
        return comicRepository.save(comic);
    }

    public Comic getComicByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new RuntimeException("Comic not found: " + title));
    }

    public Comic updateQuantityComic(Comic comic, int quantity) {
        comic.setQuantity(quantity);
        return comicRepository.save(comic);
    }

    public Comic updateComic(Comic comic) {
        return comicRepository.save(comic);
    }

    public List<Comic> findByFilter(String keyword) {
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(keyword, keyword);
    }

    public void outOfStockToggle() {
        List<Comic> comics = comicRepository.findAll();
        for (Comic comic : comics) {
            comic.setOutOfStock(comic.getQuantity() == 0);
        }
        comicRepository.saveAll(comics);
    }

    public List<String> findLowStock() {
        return comicRepository.findByOutOfStockTrue()
                .stream()
                .map(Comic::getTitle)
                .toList();
    }
}