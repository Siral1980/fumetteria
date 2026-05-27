package it.apuliadigital.comicstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.ComicService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sells")
public class SellController {
    @Autowired
    private ComicService comicService;

    @PostMapping("/sellComicWithRecord")
    public ResponseEntity<Sell> sellComicWithSell(@RequestParam String title, @RequestParam int quantity) {
        Sell sell = comicService.sellComic(title, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(sell);
    }

    @GetMapping("/byDateRange")
    public ResponseEntity<List<Sell>> getSellsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Sell> sells = comicService.findSalesByDateRange(startDate, endDate);
        return ResponseEntity.ok(sells);
    }

    @GetMapping("/byAmountGreaterThan")
    public ResponseEntity<List<Sell>> getSellsByAmountGreaterThan(@RequestParam double amount) {
        List<Sell> sells = comicService.findSalesByAmountGreaterThan(BigDecimal.valueOf(amount));
        return ResponseEntity.ok(sells);
    }
}
