package it.apuliadigital.comicstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// risposta della vendita, restituisce le info principali
// non restituisco l'oggetto Sell diretto perché ha la relazione con Comic
// e potrebbe creare problemi di serializzazione (loop infinito)
@Data
@AllArgsConstructor
public class SellResponse {
    private Long id;
    private String comicTitle;  // solo il titolo, non tutto l'oggetto comic
    private Integer quantitySold;
    private LocalDateTime soldAt;
    private BigDecimal totalPrice;
}
