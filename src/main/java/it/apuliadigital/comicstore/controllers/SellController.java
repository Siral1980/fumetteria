package it.apuliadigital.comicstore.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.apuliadigital.comicstore.SellDTO;
import it.apuliadigital.comicstore.services.SellService;

@RestController
@RequestMapping("/sells")
public class SellController {
    
    @Autowired
    private SellService sellService;
    @GetMapping("/findByRange")
    public ResponseEntity<?> findByDateRange(
 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        try {
            List<SellDTO> sells = sellService.findByRange(start, end);
            return ResponseEntity.ok(sells);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/findByAmount")
    public ResponseEntity<?> findByAmountGreaterThan(@RequestParam double amount) {
        try {
            List<SellDTO> sells = sellService.findByAmountGreaterThan(amount);
            return ResponseEntity.ok(sells);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
