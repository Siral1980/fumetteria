package it.apuliadigital.comicstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

// DTO per l'aggiornamento - uguale a create ma è separato
// perché in futuro potrebbero servire campi diversi tra create e update
// inoltre così è più chiaro cosa si può modificare (no id, no quantity)
@Data
public class ComicUpdateRequest {

    @NotBlank(message = "Il titolo è obbligatorio")
    private String title;

    @NotBlank(message = "L'autore è obbligatorio")
    private String author;

    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere positivo")
    private BigDecimal price;

    @NotBlank(message = "Il genere è obbligatorio")
    private String genre;
}
