package it.apuliadigital.comicstore.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.repositories.ComicRepository;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    public Comic addComic(Comic comic){
        if(comic == null){
            throw new IllegalArgumentException("I dati del fumetto sono obbligatori");
        }
        if(comic.getTitle() == null || comic.getTitle().isBlank()){
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio");
        }

        comic.setQuantity(0);
        comic.setOutOfStock(true);
        return comicRepository.save(comic);
    }

    public Comic findComicByTitle(String title){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("Il titolo da cercare è obbligatorio");
        }
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));
    }

    public List<Comic> findComicsByFilter(String filter){
        if(filter == null || filter.isBlank()){
            throw new IllegalArgumentException("La stringa di ricerca è obbligatoria");
        }
        return comicRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(filter, filter);
    }

    public Comic stockComic(String title, int quantityToAdd){
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }
        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("La quantità da aggiungere deve essere maggiore di 0.");
        }

        Comic comic = comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

        int currentQuantity = comic.getQuantity();
        comic.setQuantity(currentQuantity + quantityToAdd);
        comic.setOutOfStock(comic.getQuantity() == 0);

        return comicRepository.save(comic);
    }

    public Comic sellComic(String title, int quantityToSell) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }
        if (quantityToSell <= 0) {
            throw new IllegalArgumentException("La quantità da vendere deve essere maggiore di 0.");
        }

        Comic comic = comicRepository.findByTitle(title)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

        int currentQuantity = comic.getQuantity();
        if (currentQuantity < quantityToSell) {
            throw new IllegalStateException("Copie insufficienti. Copie disponibili: " + currentQuantity + ".");
        }

        comic.setQuantity(currentQuantity - quantityToSell);
        comic.setOutOfStock(comic.getQuantity() == 0);

        return comicRepository.save(comic);
    }

    @Transactional
    public List<Comic> toggleOutOfStock() {
        List<Comic> comics = comicRepository.findAll();
        for (Comic comic : comics) {
            comic.setOutOfStock(comic.getQuantity() == 0);
        }
        return comicRepository.saveAll(comics);
    }

    public List<String> findLowStockTitles() {
        return comicRepository.findByOutOfStockTrue().stream()
                .map(Comic::getTitle)
                .toList();
    }

    public Comic updateComic(Long id, Comic comicDetails) {
        if (comicDetails == null) {
            throw new IllegalArgumentException("I dati del fumetto sono obbligatori.");
        }
        if (id == null) {
            throw new IllegalArgumentException("L'id del fumetto è obbligatorio.");
        }
        if (comicDetails.getTitle() == null || comicDetails.getTitle().isBlank()) {
            throw new IllegalArgumentException("Il titolo del fumetto è obbligatorio.");
        }

        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato."));

        comic.setTitle(comicDetails.getTitle());
        comic.setAuthor(comicDetails.getAuthor());
        comic.setPrice(comicDetails.getPrice());
        comic.setGenre(comicDetails.getGenre());

        return comicRepository.save(comic);
    }
}
