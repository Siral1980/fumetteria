package it.apuliadigital.comicstore.controllers;

import it.apuliadigital.comicstore.models.Sell;
import it.apuliadigital.comicstore.services.SellService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

    
    @GetMapping("/by-date")
    public ResponseEntity<List<Sell>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        List<Sell> sells = sellService.findByDateRange(from, to);
        return ResponseEntity.ok(sells);
    }

    @GetMapping("/by-amount")
    public ResponseEntity<List<Sell>> findByAmountGreaterThan(@RequestParam BigDecimal min) {
        List<Sell> sells = sellService.findByAmountGreaterThan(min);
        return ResponseEntity.ok(sells);
    }
}