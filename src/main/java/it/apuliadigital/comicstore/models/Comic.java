package it.apuliadigital.comicstore.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private String publisher;

    @Column(name = "publication_year")
    private Integer year;

    private String genre;
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity = 0;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean outOfStock = true;

    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Sell> sells = new ArrayList<>();
}
