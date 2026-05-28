package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/sells")
public class SellController {

    @Autowired
    private SellService sellService;

    // ========================================================================
    // Sell Comic History - Creation of sell record
    // ========================================================================
    @PostMapping
    public ResponseEntity<Sell> sellComicHistory(@RequestParam Long comicId, @RequestParam int quantity) {
        Sell sell = sellService.sellComicHistory(comicId, quantity);
        return new ResponseEntity<>(sell, HttpStatus.CREATED);
    }

    // ========================================================================
    // Find sales in a date range
    // ========================================================================
    @GetMapping("/by-date")
    public ResponseEntity<List<Sell>> findByDateRange(
            @Parameter(example = "YYYY/MM/DD") @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate from, 
            @Parameter(example = "YYYY/MM/DD") @RequestParam @DateTimeFormat(pattern = "yyyy/MM/dd") LocalDate to) {
        List<Sell> sells = sellService.findByDateRange(from.atStartOfDay(), to.atTime(23, 59, 59));
        return ResponseEntity.ok(sells);
    }

    // ========================================================================
    // Find sales greater than amount
    // ========================================================================
    @GetMapping("/by-amount")
    public ResponseEntity<List<Sell>> findByAmountGreaterThan(@RequestParam BigDecimal min) {
        List<Sell> sells = sellService.findByAmountGreaterThan(min);
        return ResponseEntity.ok(sells);
    }
}
