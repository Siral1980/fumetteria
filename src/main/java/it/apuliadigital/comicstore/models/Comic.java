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

    // AGGIUNGIAMO IL TITOLO (Obbligatorio e Univoco come richiesto)
    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private int quantity = 0;

    private String author;

    private Double price;

    private String genre;
}