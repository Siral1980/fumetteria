package it.apuliadigital.comicstore.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;

@RestController
@RequestMapping("/api/sells")
public class SellController {

    @Autowired
    private SellService sellService;

    @PutMapping("/sellComicWithSell")
    public ResponseEntity<Sell> sellComicWithSell(@RequestParam String title, @RequestParam int quantity) {

        Sell sell = sellService.sellComicWithSell(title, quantity);

        return ResponseEntity.ok(sell);
    }

    @GetMapping("/findByDateRange")
    public ResponseEntity<List<Sell>> findSellsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<Sell> sells = sellService.findSellsByDateRange(startDate, endDate);

        return ResponseEntity.ok(sells);
    }

    @GetMapping("/findGreaterThanAmount")
    public ResponseEntity<List<Sell>> findSellsGreaterThanAmount(@RequestParam BigDecimal amount) {

        List<Sell> sells = sellService.findSellsGreaterThanAmount(amount);

        return ResponseEntity.ok(sells);
    }
}