package it.apuliadigital.comicstore.controllers;

import io.swagger.v3.oas.annotations.Operation;
import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.models.annotations.OpenAPI30;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sales")
public class SellController {

    @Autowired
    private SellService sellService;

    // Registra una vendita di fumetti con la quantità specificata
    @PostMapping("/sell")
    @Operation(summary = "Vendi dei fumetti")
    public ResponseEntity<Sell> sellComic(@RequestParam Long comicId, @RequestParam int quantity) {
        return ResponseEntity.ok(sellService.sellComicHistory(comicId, quantity));
    }

    // Restituisce le vendite effettuate tra due date specifiche
    @GetMapping("/by-date")
    @Operation(summary = "Trova vendite in un intervallo di date")
    public ResponseEntity<List<Sell>> getSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(sellService.findSalesInDateRange(start, end));
    }

    // Restituisce le vendite con importo totale maggiore del valore richiesto
    @GetMapping("/high-amount")
    @Operation(summary = "Trova vendite con un importo superiore a un certo valore")
    public ResponseEntity<List<Sell>> getHighAmountSales(@RequestParam BigDecimal minAmount) {
        return ResponseEntity.ok(sellService.findSalesWithAmountGreaterThan(minAmount));
    }
}