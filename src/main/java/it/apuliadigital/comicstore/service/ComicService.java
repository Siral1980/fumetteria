package it.apuliadigital.comicstore.service;

import it.apuliadigital.comicstore.dto.*;
import it.apuliadigital.comicstore.exception.ComicAlreadyExistsException;
import it.apuliadigital.comicstore.exception.ComicNotFoundException;
import it.apuliadigital.comicstore.exception.InsufficientStockException;
import it.apuliadigital.comicstore.model.Comic;
import it.apuliadigital.comicstore.repository.ComicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// qui sta tutta la logica di business, il controller non deve sapere come funziona
// @RequiredArgsConstructor di lombok crea il costruttore con i campi final (injection)
@Service
@RequiredArgsConstructor
@Slf4j  
public class ComicService {

    private final ComicRepository comicRepository;

    // aggiunge un nuovo fumetto al catalogo
    // quantity parte da 0 e outOfStock da true per specifica
    @Transactional
    public Comic addComic(ComicCreateRequest request) {
        // prima controllo che non esista già un fumetto con lo stesso titolo
        if (comicRepository.findByTitleIgnoreCase(request.getTitle()).isPresent()) {
            throw new ComicAlreadyExistsException(
                    "Esiste già un fumetto con il titolo: " + request.getTitle());
        }
        // uso il builder per creare l'oggetto, più leggibile del costruttore
        Comic comic = Comic.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .price(request.getPrice())
                .genre(request.getGenre())
                .quantity(0)
                .outOfStock(true)
                .build();
        log.debug("Creazione fumetto: {}", comic.getTitle());
        return comicRepository.save(comic);
    }

    // cerca un fumetto per titolo esatto (case insensitive)
    // restituisce eccezione se non lo trova
    @Transactional(readOnly = true)
    public Comic findByTitle(String title) {
        return comicRepository.findByTitleIgnoreCase(title)
                .orElseThrow(() -> new ComicNotFoundException(
                        "Fumetto non trovato con titolo: " + title));
    }

    // aggiunge copie in magazzino (carico merce)
    // la quantità si somma a quella già presente
    @Transactional
    public Comic stockComic(Long id, QuantityRequest request) {
        Comic comic = findById(id);
        comic.setQuantity(comic.getQuantity() + request.getQuantity());
        log.debug("Stock aggiornato per '{}': +{} copie", comic.getTitle(), request.getQuantity());
        return comicRepository.save(comic);
    }

    // vende delle copie del fumetto
    // prima controlla che ci siano abbastanza copie disponibili
    @Transactional
    public Comic sellComic(Long id, QuantityRequest request) {
        Comic comic = findById(id);
        // controllo fondamentale: non posso vendere più di quello che ho
        if (comic.getQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(
                    String.format("Copie disponibili: %d, richieste: %d",
                            comic.getQuantity(), request.getQuantity()));
        }
        comic.setQuantity(comic.getQuantity() - request.getQuantity());
        log.debug("Vendita per '{}': -{} copie", comic.getTitle(), request.getQuantity());
        return comicRepository.save(comic);
    }

    // aggiorna i dati del fumetto
    // non si può cambiare id (ovvio) né quantity (quello lo fanno stock e sell)
    @Transactional
    public Comic updateComic(Long id, ComicUpdateRequest request) {
        Comic comic = findById(id);
        // se cambio titolo devo verificare che il nuovo non esista già
        if (!comic.getTitle().equalsIgnoreCase(request.getTitle())) {
            comicRepository.findByTitleIgnoreCase(request.getTitle()).ifPresent(c -> {
                throw new ComicAlreadyExistsException(
                        "Esiste già un fumetto con il titolo: " + request.getTitle());
            });
        }
        comic.setTitle(request.getTitle());
        comic.setAuthor(request.getAuthor());
        comic.setPrice(request.getPrice());
        comic.setGenre(request.getGenre());
        log.debug("Fumetto aggiornato: {}", comic.getTitle());
        return comicRepository.save(comic);
    }

    // ricerca per testo su titolo e autore, anche parziale
    // può restituire lista vuota se non trova niente, non lancia eccezione
    @Transactional(readOnly = true)
    public List<Comic> findByFilter(String query) {
        return comicRepository.findByTitleOrAuthorContaining(query);
    }

    // cicla tutti i fumetti e aggiorna outOfStock in base alla quantity
    // se quantity > 0 -> outOfStock = false, altrimenti true
    @Transactional
    public int toggleOutOfStock() {
        List<Comic> all = comicRepository.findAll();
        for (Comic c : all) {
            c.setOutOfStock(c.getQuantity() == 0);
        }
        comicRepository.saveAll(all);
        long outCount = all.stream().filter(Comic::getOutOfStock).count();
        log.debug("Toggle outOfStock completato. Esauriti: {}/{}", outCount, all.size());
        return all.size();
    }

    // restituisce solo i titoli dei fumetti esauriti (outOfStock = true)
    // uso stream e map per trasformare la lista di Comic in lista di soli titoli
    @Transactional(readOnly = true)
    public List<ComicTitleResponse> findOutOfStock() {
        return comicRepository.findByOutOfStockTrue()
                .stream()
                .map(c -> new ComicTitleResponse(c.getTitle()))
                .toList();
    }

    // metodo privato di supporto per trovare un fumetto per id
    // lo uso spesso nei metodi sopra così non ripeto il codice
    public Comic findById(Long id) {
        return comicRepository.findById(id)
                .orElseThrow(() -> new ComicNotFoundException(
                        "Fumetto non trovato con id: " + id));
    }
}
