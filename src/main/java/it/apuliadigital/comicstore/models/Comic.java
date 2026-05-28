package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comic")
@Data   //c'è data quindi fa automaticamente
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String author;

    private Double price;

    private String genre;

    private int quantity = 0 ;
    @Column(name = "out_of_stock", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean outOfStock;

}
