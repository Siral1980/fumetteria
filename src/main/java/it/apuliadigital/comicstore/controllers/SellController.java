package it.apuliadigital.comicstore.controllers; // Pacchetto controller.
 
import java.math.BigDecimal; // Tipo importo.
import java.time.LocalDateTime; // Data e ora.
import java.util.List; // Lista di vendite.
 
import org.springframework.beans.factory.annotation.Autowired; // Inserisce il service.
import org.springframework.format.annotation.DateTimeFormat; // Dice a Spring come leggere date dall'URL.
import org.springframework.http.ResponseEntity; // Risposta HTTP.
import org.springframework.web.bind.annotation.GetMapping; // GET.
import org.springframework.web.bind.annotation.PutMapping; // PUT.
import org.springframework.web.bind.annotation.RequestMapping; // URL base.
import org.springframework.web.bind.annotation.RequestParam; // Parametri URL.
import org.springframework.web.bind.annotation.RestController; // Controller REST.
 
import it.apuliadigital.comicstore.models.Sell; // Model Sell.
import it.apuliadigital.comicstore.services.SellService; // Service Sell.
 
@RestController // Risponde in JSON.
@RequestMapping("/api/sells") // URL base per vendite.
public class SellController {
 
    @Autowired
    private SellService sellService; // Service delle vendite.
 
    @PutMapping("/sellComicWithSell") // PUT /api/sells/sellComicWithSell?title=...&quantity=...
    public ResponseEntity<Sell> sellComicWithSell(@RequestParam String title, @RequestParam int quantity) {
 
        Sell sell = sellService.sellComicWithSell(title, quantity); // Chiamo il service.
 
        return ResponseEntity.ok(sell); // Restituisco la vendita creata.
    }
 
    @GetMapping("/findByDateRange") // GET /api/sells/findByDateRange?startDate=...&endDate=...
    public ResponseEntity<List<Sell>> findSellsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
 
        List<Sell> sells = sellService.findSellsByDateRange(startDate, endDate); // Cerco vendite tra due date.
 
        return ResponseEntity.ok(sells); // Restituisco una lista.
    }
 
    @GetMapping("/findGreaterThanAmount") // GET /api/sells/findGreaterThanAmount?amount=30
    public ResponseEntity<List<Sell>> findSellsGreaterThanAmount(@RequestParam BigDecimal amount) {
 
        List<Sell> sells = sellService.findSellsGreaterThanAmount(amount); // Cerco vendite sopra importo.
 
        return ResponseEntity.ok(sells); // Restituisco la lista.
    }
}