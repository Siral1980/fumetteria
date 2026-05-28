package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sells")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;

    @Column(name = "quantity", nullable = false)
    private int quantity;
    @Column(name = "sale_date", nullable = false)
    private LocalDateTime saleDate;
    @Column(name = "total_price", nullable = false)
    private double totalPrice;
}