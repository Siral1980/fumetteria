package it.apuliadigital.comicstore.services; // Pacchetto service.
 
import java.math.BigDecimal; // Per calcolare importi precisi.
import java.time.LocalDateTime; // Per data e ora.
import java.util.List; // Per restituire liste di vendite.
import java.util.NoSuchElementException; // Errore se non trovo il fumetto.
import org.springframework.beans.factory.annotation.Autowired; // Dependency injection.
import org.springframework.stereotype.Service; // Classe service.
import org.springframework.transaction.annotation.Transactional; // Gestisce operazioni collegate come un'unica transazione.
 
import it.apuliadigital.comicstore.models.Comic; // Model Comic.
import it.apuliadigital.comicstore.models.Sell; // Model Sell.
import it.apuliadigital.comicstore.repositories.ComicRepository; // Repository Comic.
import it.apuliadigital.comicstore.repositories.SellRepository; // Repository Sell.
 
@Service // Spring gestisce questa classe come service.
public class SellService {
 
    @Autowired
    private SellRepository sellRepository; // Serve per salvare e cercare vendite.
 
    @Autowired
    private ComicRepository comicRepository; // Serve per cercare e aggiornare fumetti.
 
    @Transactional // Se qualcosa fallisce, Spring annulla tutta l'operazione.
    public Sell sellComicWithSell(String title, int quantityToSell) { // Vendita con registrazione nella tabella sell.
 
        if (title == null || title.isBlank()) { // Controllo titolo.
            throw new IllegalArgumentException("Il titolo del fumetto e obbligatorio.");
        }
        if (quantityToSell <= 0) { // Controllo quantita.
            throw new IllegalArgumentException("La quantita da vendere deve essere maggiore di 0.");
        }
 
        Comic comic = comicRepository.findByTitle(title) // Cerco il fumetto.
                .orElseThrow(() -> new NoSuchElementException("Fumetto non trovato.")); // Se non esiste, errore.
 
        if (comic.getPrice() == null) { // Senza prezzo non posso calcolare il totale.
            throw new IllegalStateException("Il fumetto non ha un prezzo valido.");
        }
 
        int currentQuantity = comic.getQuantity(); // Leggo copie disponibili.
 
        if (currentQuantity < quantityToSell) { // Controllo disponibilita.
            throw new IllegalStateException("Copie insufficienti. Copie disponibili: " + currentQuantity + ".");
        }
 
        comic.setQuantity(currentQuantity - quantityToSell); // Aggiorno il magazzino.
        comicRepository.save(comic); // Salvo il fumetto aggiornato.
 
        BigDecimal totalAmount = BigDecimal.valueOf(comic.getPrice()) // Trasformo il prezzo in BigDecimal.
                .multiply(BigDecimal.valueOf(quantityToSell)); // Totale = prezzo * quantita venduta.
 
        Sell sell = new Sell(); // Creo una vendita nuova.
        sell.setComic(comic); // Dico quale fumetto e stato venduto.
        sell.setSellingQuantity(quantityToSell); // Salvo quante copie sono state vendute.
        sell.setSellingDate(LocalDateTime.now()); // Salvo data e ora attuali.
        sell.setTotalAmount(totalAmount); // Salvo il prezzo totale.
        
 
        return sellRepository.save(sell); // Salvo la vendita e la restituisco.
    }
 
    public List<Sell> findSellsByDateRange(LocalDateTime startDate, LocalDateTime endDate) { // Cerca vendite tra due date.
 
        if (startDate == null || endDate == null) { // Le date sono obbligatorie.
            throw new IllegalArgumentException("La data iniziale e la data finale sono obbligatorie.");
            }
 
        if (startDate.isAfter(endDate)) { // La data iniziale non puo venire dopo quella finale.
            throw new IllegalArgumentException("La data iniziale non puo essere successiva alla data finale.");
        }
 
        return sellRepository.findBySellingDateBetween(startDate, endDate); // Query al database.
    }
 
    public List<Sell> findSellsGreaterThanAmount(BigDecimal amount) { // Cerca vendite sopra un importo.
 
        if (amount == null) { // L'importo e obbligatorio.
            throw new IllegalArgumentException("L'importo e obbligatorio.");
        }
 
        if (amount.compareTo(BigDecimal.ZERO) < 0) { // Importo negativo non valido.
            throw new IllegalArgumentException("L'importo non puo essere negativo.");
        }
 
        return sellRepository.findByTotalAmountGreaterThan(amount); // Query al database.
    }
}