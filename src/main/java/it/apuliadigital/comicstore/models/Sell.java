package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "sells")
@Data
public class Sell {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @Column(nullable = false)
    private LocalDateTime sellDate;

    
    @Column(nullable = false)
    private int quantitySold;

    
    @Column(nullable = false)
    private Double totalPrice;

   
    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false)
    private Comic comic;
}