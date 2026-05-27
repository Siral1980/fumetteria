package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a sale transaction in the store.
 */
@Entity
@Table(name = "sell")
@Data
public class Sell {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "Selling date is required")
    private LocalDateTime sellingDate;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int sellingQuantity;

    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    @NotNull(message = "Total amount is required")
    private BigDecimal totalAmount; // comicPrice * sellingQuantity

    @NotNull(message = "Comic is required")
    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;
}
