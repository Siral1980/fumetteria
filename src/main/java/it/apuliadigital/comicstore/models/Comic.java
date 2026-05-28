package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "comic")
@Data
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    private String author;

    private Double price;

    private String genre;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int quantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private boolean outOfStock = true;

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.outOfStock = (this.quantity <= 0);
    }
}
