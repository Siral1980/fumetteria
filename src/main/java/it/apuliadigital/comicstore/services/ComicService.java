package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.exceptions.BadRequestException;
import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    public Comic addComic(Comic comic) {
        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    public Comic findByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Comic not found with title: " + title));
    }

    public Comic stockComic(Long id, int quantityToAdd) {
        if (quantityToAdd <= 0) {
            throw new BadRequestException("Quantity to add must be greater than 0");
        }
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic not found with id: " + id));

        comic.setQuantity(comic.getQuantity() + quantityToAdd);
        if (comic.getQuantity() > 0) {
            comic.setOutOfStock(false);
        }
        return comicRepository.save(comic);
    }

    public Comic updateComic(Long id, Comic comicDetails) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic not found with id: " + id));

        comic.setTitle(comicDetails.getTitle());
        comic.setAuthor(comicDetails.getAuthor());
        comic.setPrice(comicDetails.getPrice());
        comic.setGenre(comicDetails.getGenre());

        return comicRepository.save(comic);
    }

    public List<Comic> findByFilter(String query) {
        return comicRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(query, query);
    }

    public void toggleOutOfStock() {
        List<Comic> allComics = comicRepository.findAll();
        for (Comic comic : allComics) {
            comic.setOutOfStock(comic.getQuantity() <= 0);
        }
        comicRepository.saveAll(allComics);
    }

    public List<String> findLowStock() {
        return comicRepository.findByOutOfStockTrue()
                .stream()
                .map(Comic::getTitle)
                .collect(Collectors.toList());
    }

    public Comic findById(Long id) {
        return comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic not found with id: " + id));
    }

    public Comic saveComic(Comic comic) {
        return comicRepository.save(comic);
    }
}
