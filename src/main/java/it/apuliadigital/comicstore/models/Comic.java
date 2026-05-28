package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "comic")
@Data
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private int quantity = 0;

    private String author;

    private Double price;

    private String genre;

    // Task 9: outOfStock impostato a true di default alla creazione (visto che quantity parte da 0)
    @Column(nullable = false)
    private boolean outOfStock = true;
}