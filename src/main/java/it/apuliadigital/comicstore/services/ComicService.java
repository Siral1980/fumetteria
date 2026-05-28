package it.apuliadigital.comicstore.services;

import it.apuliadigital.comicstore.models.Comic;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.repositories.ComicRepository;
import it.apuliadigital.comicstore.repositories.SellRepository;
import it.apuliadigital.comicstore.exceptions.ResourceNotFoundException;
import it.apuliadigital.comicstore.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComicService {

    @Autowired
    private ComicRepository comicRepository;

    @Autowired
    private SellRepository sellRepository; 

    
    private void updateStockStatus(Comic comic) {
        comic.setOutOfStock(comic.getQuantity() <= 0);
    }

    public Comic addComic(Comic comic) {
        comic.setQuantity(0); 
        comic.setOutOfStock(true); 
        return comicRepository.save(comic);
    }

    public Comic findComicByTitle(String title) {
        return comicRepository.findByTitle(title)
                .orElseThrow(() -> new ResourceNotFoundException("Fumetto con titolo '" + title + "' non trovato."));
    }

    public Comic stockComic(Long id, int quantityToAdd) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fumetto non trovato con ID: " + id)); 
        
        comic.setQuantity(comic.getQuantity() + quantityToAdd);
        updateStockStatus(comic); 
        
        return comicRepository.save(comic);
    }

    
    public Comic sellComic(Long id, int quantityToSell) {
        Comic comic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fumetto non trovato con ID: " + id)); 
        
        if (comic.getQuantity() < quantityToSell) {
            throw new BadRequestException("Disponibilità insufficiente. Copie presenti: " + comic.getQuantity()); 
        }
        
       
        comic.setQuantity(comic.getQuantity() - quantityToSell);
        updateStockStatus(comic); 
        Comic savedComic = comicRepository.save(comic);

       
        Sell sell = new Sell();
        sell.setComic(savedComic);
        sell.setQuantitySold(quantityToSell);
        double price = (comic.getPrice() != null) ? comic.getPrice() : 0.0;
        sell.setTotalPrice(price * quantityToSell);
        sell.setSellDate(LocalDateTime.now());
        
        sellRepository.save(sell); 
        return savedComic;
    }

    public Comic updateComic(Long id, Comic incomingComic) {
        Comic existingComic = comicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fumetto non trovato con ID: " + id));
        
        existingComic.setTitle(incomingComic.getTitle());
        existingComic.setAuthor(incomingComic.getAuthor());
        existingComic.setPrice(incomingComic.getPrice());
        existingComic.setGenre(incomingComic.getGenre());
        
        return comicRepository.save(existingComic);
    }

    public List<Comic> searchByKeyword(String keyword) {
        return comicRepository.findByAuthorContainingIgnoreCaseOrTitleContainingIgnoreCase(keyword, keyword);
    }

    public List<String> getOutOfStockTitles() {
        return comicRepository.findTitlesByOutOfStockTrue();
    }

    
    public List<Sell> getAllSells() {
        return sellRepository.findAll();
    }
}