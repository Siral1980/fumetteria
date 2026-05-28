package it.apuliadigital.comicstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// entity per tenere traccia delle vendite
// ogni volta che vendo un fumetto creo un record qui
@Entity
@Table(name = "sell")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // quale fumetto è stato venduto - relazione molti a uno
    // ManyToOne perché molte vendite possono riguardare lo stesso fumetto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;

    // quante copie sono state vendute in questa transazione
    @Column(nullable = false)
    private Integer quantitySold;

    // data e ora della vendita, la salvo automaticamente nel service
    @Column(nullable = false)
    private LocalDateTime soldAt;

    // prezzo totale = prezzo unitario * quantità venduta
    @Column(nullable = false)
    private BigDecimal totalPrice;
}
