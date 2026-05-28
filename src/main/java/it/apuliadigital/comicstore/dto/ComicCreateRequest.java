package it.apuliadigital.comicstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

// DTO per la creazione di un fumetto
// non passo id (lo genera il db) e non passo quantity (parte sempre da 0)
@Data
public class ComicCreateRequest {

    @NotBlank(message = "Il titolo è obbligatorio")
    private String title;

    @NotBlank(message = "L'autore è obbligatorio")
    private String author;

    // NotNull perché BigDecimal non è una stringa, NotBlank non funzionerebbe
    @NotNull(message = "Il prezzo è obbligatorio")
    @Positive(message = "Il prezzo deve essere positivo")
    private BigDecimal price;

    @NotBlank(message = "Il genere è obbligatorio")
    private String genre;
}
