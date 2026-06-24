package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sells")
public class SellController {

    @Autowired
    private SellService sellService;

    @GetMapping("/search/date")
    public ResponseEntity<List<Sell>> findSellsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return new ResponseEntity<>(sellService.findSellsByDateRange(startDate, endDate), HttpStatus.OK);
    }

    @GetMapping("/search/amount")
    public ResponseEntity<List<Sell>> findSellsByAmountGreaterThan(
            @RequestParam BigDecimal minAmount) {
        return new ResponseEntity<>(sellService.findSellsByAmountGreaterThan(minAmount), HttpStatus.OK);
    }
}
