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

    private String author;

    private Double price;

    private String genre;

    private int quantity;

    // Task 9: default true perché ogni nuovo fumetto parte con quantity = 0
    private boolean outOfStock = true;
}