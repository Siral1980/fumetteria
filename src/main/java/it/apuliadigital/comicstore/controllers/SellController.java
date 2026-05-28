package it.apuliadigital.comicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sells")
public class SellController {

    @Autowired
    private SellService sellService;

    //  sellComicHistory
    @PostMapping("/sellComicWithRecord")
    public ResponseEntity<Sell> sellComicWithRecord(
            @RequestParam String title,
            @RequestParam int quantity) {
        Sell sell = sellService.sellComicHistory(title, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sell);
    }

    // Ricerca vendite per range di date
    @GetMapping("/byDateRange")
    public ResponseEntity<List<Sell>> getSellsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Sell> sells = sellService.findSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(sells);
    }

    // Ricerca vendite per importo superiore
    @GetMapping("/byAmountGreaterThan")
    public ResponseEntity<List<Sell>> getSellsByAmountGreaterThan(
            @RequestParam double amount) {
        List<Sell> sells = sellService.findSalesByAmountGreaterThan(BigDecimal.valueOf(amount));
        return ResponseEntity.ok(sells);
    }
}