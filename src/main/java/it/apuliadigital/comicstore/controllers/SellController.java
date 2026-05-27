package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sells")
@Tag(name = "Sales Management", description = "APIs for managing comic sales")
public class SellController {

    @Autowired
    private SellService sellService;

    /**
     * Sell a comic and create a sell record
     */
    @PostMapping
    @Operation(summary = "Sell a comic", description = "Sells a comic and creates a sell record with comic, quantity, date and total price")
    public ResponseEntity<Sell> sellComicWithSell(
            @RequestParam Long comicId,
            @RequestParam int quantity) {
        if (quantity <= 0) {
            return ResponseEntity.badRequest().build();
        }
        Sell sell = sellService.sellComicWithSell(comicId, quantity);
        return new ResponseEntity<>(sell, HttpStatus.CREATED);
    }

    /**
     * Find sales within a date range
     */
    @GetMapping("/by-date-range")
    @Operation(summary = "Find sales by date range", description = "Finds all sales within a specified date range")
    public ResponseEntity<List<Sell>> findSalesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Sell> sales = sellService.findSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(sales);
    }

    /**
     * Find sales with amount greater than specified value
     */
    @GetMapping("/by-amount")
    @Operation(summary = "Find sales by amount", description = "Finds all sales with total amount greater than specified value")
    public ResponseEntity<List<Sell>> findSalesByAmountGreaterThan(
            @RequestParam BigDecimal amount) {
        List<Sell> sales = sellService.findSalesByAmountGreaterThan(amount);
        return ResponseEntity.ok(sales);
    }
}
