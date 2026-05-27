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
    private LocalDateTime sellingDate;
    private int sellingQuantity;
    private BigDecimal totalAmount; // comicPrice * sellingQuantity
    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;
}
