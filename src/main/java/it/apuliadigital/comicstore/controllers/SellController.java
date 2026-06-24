package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sells")
public class SellController {

    private final SellService sellService;

    public SellController(SellService sellService) {
        this.sellService = sellService;
    }

    @GetMapping("/range")
    public ResponseEntity<List<Sell>> findSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(sellService.findSalesByDateRange(from, to));
    }

    @GetMapping("/min-amount")
    public ResponseEntity<List<Sell>> findSalesAboveAmount(@RequestParam BigDecimal amount) {
        return ResponseEntity.ok(sellService.findSalesAboveAmount(amount));
    }
}