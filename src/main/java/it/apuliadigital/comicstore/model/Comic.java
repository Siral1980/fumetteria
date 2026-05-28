package it.apuliadigital.comicstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

// questa classe rappresenta la tabella comic nel database
// con @Entity spring sa che deve mapparla
@Entity
@Table(name = "comic")
@Data               // lombok genera getter e setter automaticamente, comodissimo
@NoArgsConstructor  // costruttore vuoto serve a JPA
@AllArgsConstructor // costruttore con tutti i campi
@Builder            // per creare oggetti in modo più leggibile
public class Comic {

    // id generato automaticamente dal database, non lo passo io
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // unique = true perché il titolo non deve essere duplicato (da specifica)
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Il titolo è obbligatorio")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "L'autore è obbligatorio")
    private String author;

    // uso BigDecimal per i soldi perché con double ci possono essere problemi di arrotondamento
    @Column(nullable = false)
    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere positivo")
    private BigDecimal price;

    @Column(nullable = false)
    @NotBlank(message = "Il genere è obbligatorio")
    private String genre;

    // quando creo un fumetto la quantità parte da 0
    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;

    // se quantity è 0 il fumetto è esaurito, quindi outOfStock parte a true
    @Column(nullable = false)
    @Builder.Default
    private Boolean outOfStock = true;

    // relazione con la tabella sell, un fumetto può avere più vendite
    // mappedBy dice a JPA che la foreign key sta nell'altra tabella
    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sell> sells;
}
