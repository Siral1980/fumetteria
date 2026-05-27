package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "comics")
@Data
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String author;
    private String publisher;
    private Integer year;
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity = 0;

    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL)
    private List<Sell> sells;
}