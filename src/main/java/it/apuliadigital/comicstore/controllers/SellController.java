package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sells")
public class SellController {

    @Autowired
    private SellService sellService;

    // ========================================================================
    // BONUS TASK 11: Sell Comic History
    // Creazione del record di vendita
    // ========================================================================
    @PostMapping
    public ResponseEntity<Sell> sellComicHistory(@RequestParam Long comicId, @RequestParam int quantity) {
        Sell sell = sellService.sellComicHistory(comicId, quantity);
        return new ResponseEntity<>(sell, HttpStatus.CREATED);
    }

    // ========================================================================
    // BONUS TASK 11a: Ricerca vendite in un range di date
    // ========================================================================
    @GetMapping("/by-date")
    public ResponseEntity<List<Sell>> findByDateRange(@RequestParam LocalDateTime from, @RequestParam LocalDateTime to) {
        List<Sell> sells = sellService.findByDateRange(from, to);
        return ResponseEntity.ok(sells);
    }

    // ========================================================================
    // BONUS TASK 11b: Ricerca vendite superiore ad un determinato importo
    // ========================================================================
    @GetMapping("/by-amount")
    public ResponseEntity<List<Sell>> findByAmountGreaterThan(@RequestParam BigDecimal min) {
        List<Sell> sells = sellService.findByAmountGreaterThan(min);
        return ResponseEntity.ok(sells);
    }
}
