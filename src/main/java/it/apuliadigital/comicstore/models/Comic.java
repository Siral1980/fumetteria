package it.apuliadigital.comicstore.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "out_of_stock", nullable = false, columnDefinition = "boolean default true")
    private boolean outOfStock = true;
}
