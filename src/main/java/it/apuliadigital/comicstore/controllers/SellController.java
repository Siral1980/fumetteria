package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SellController {

    @Autowired
    private ComicService comicService;

    @PostMapping("/sell/{id}")
    public ResponseEntity<Sell> sellComicWithRecord(@PathVariable Long id, @RequestParam int quantity) {
        return new ResponseEntity<>(comicService.sellComicWithSell(id, quantity), HttpStatus.CREATED);
    }

    @GetMapping("/range")
    public ResponseEntity<List<Sell>> getSalesByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return new ResponseEntity<>(comicService.findSalesInDateRange(start, end), HttpStatus.OK);
    }

    // CORRETTO: Rimosso il vecchio 'return null' e collegato al service
    @GetMapping("/amount")
    public ResponseEntity<List<Sell>> getSalesGreaterThan(@RequestParam double minAmount) {
        return new ResponseEntity<>(comicService.findSalesWithPriceGreaterThan(minAmount), HttpStatus.OK);
    }
}