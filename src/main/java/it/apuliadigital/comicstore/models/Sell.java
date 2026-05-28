package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sell")
@Data
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime sellingDate;      // c. data e orario della vendita

    private int sellingQuantity;            // b. quantità venduta

    private BigDecimal totalAmount;         // d. prezzo totale (comicPrice * sellingQuantity)

    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;                    // a. fumetto venduto
}