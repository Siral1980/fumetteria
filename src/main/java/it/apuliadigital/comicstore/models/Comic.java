package it.apuliadigital.comicstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * Entity representing a comic book in the store inventory.
 */
@Entity
@Table(name = "comic")
@Data
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @NotNull(message = "Price is required")
    private Double price;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;

}
