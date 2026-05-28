package it.apuliadigital.comicstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.apuliadigital.comicstore.dto.QuantityRequest;
import it.apuliadigital.comicstore.dto.SellResponse;
import it.apuliadigital.comicstore.service.SellService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// controller per le vendite e lo storico
@RestController
@RequestMapping("/api/sells")
@RequiredArgsConstructor
@Tag(name = "Sells", description = "Gestione vendite e storico")
public class SellController {

    private final SellService sellService;

    // POST /api/sells/{comicId} - vende il fumetto e salva lo storico
    // passo l'id del fumetto nel path e la quantità nel body
    // 201 perché creo un nuovo record di vendita
    @PostMapping("/{comicId}")
    @Operation(summary = "Vende un fumetto e registra la vendita nello storico")
    public ResponseEntity<SellResponse> sellComicHistory(
            @PathVariable Long comicId,
            @Valid @RequestBody QuantityRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sellService.sellComicHistory(comicId, request));
    }

    // GET /api/sells/by-date?from=2024-01-01T00:00:00&to=2024-12-31T23:59:59
    // @DateTimeFormat serve per dire a spring come parsare la data dalla query string
    @GetMapping("/by-date")
    @Operation(summary = "Cerca vendite in un range di date (ISO format: yyyy-MM-ddTHH:mm:ss)")
    public ResponseEntity<List<SellResponse>> findByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return ResponseEntity.ok(sellService.findByDateRange(from, to));
    }

    // GET /api/sells/by-amount?min=50.00 - cerca vendite con importo > min
    @GetMapping("/by-amount")
    @Operation(summary = "Cerca vendite con importo totale superiore al valore specificato")
    public ResponseEntity<List<SellResponse>> findByAmountGreaterThan(
            @RequestParam BigDecimal min) {
        return ResponseEntity.ok(sellService.findByAmountGreaterThan(min));
    }
}
